package com.nexters.phochak.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.DocumentGenerator;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.user.adapter.in.web.UserController;
import com.nexters.phochak.user.adapter.out.api.KakaoInformationFeignClient;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.port.in.KakaoUserInformation;
import com.nexters.phochak.user.application.port.in.NicknameModifyRequestDto;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends RestDocsApiTest {

    @Autowired
    UserController userController;
    @Autowired
    UserRepository userRepository;
    @MockBean
    KakaoInformationFeignClient kakaoInformationFeignClient;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userController).build();
        RestDocsApiTest.Util.setMockMvc(mockMvc);
    }

    @Test
    @DisplayName("[유저 API] 카카오 OAuth 회원가입")
    void sign_up() throws Exception {
        //given
        final OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;
        final String providerId = "newProviderId";
        final KakaoUserInformation kakaoRequestResponse = mockKakaoUserInformation(providerId);
        when(kakaoInformationFeignClient.call(any(), any())).thenReturn(kakaoRequestResponse);

        //when
        final ResultActions response = Scenario.login().request().getResponse();

        //then
        assertThat(userRepository.findByProviderAndProviderId(provider, providerId)).isPresent();

        //docs
        DocumentGenerator.login(response);
    }

    @Test
    @DisplayName("[유저 API] - 닉네임 변경")
    void modifyNickname() throws Exception {
        //given
        final String accessToken = Scenario.createUser().request()
                .advance().createAccessToken().getAccessToken();
        final String newNickname = "새로운_여행자";
        NicknameModifyRequestDto requestDto = new NicknameModifyRequestDto(newNickname);

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .put("/v1/user/nickname")
                                .header(AUTHORIZATION_HEADER, accessToken)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        assertThat(userRepository.findById(1L).get().getNickname()).isEqualTo(newNickname);

        //docs
        DocumentGenerator.modifyNickname(response);
    }

    @Test
    @DisplayName("[유저 API] 닉네임 중복확인")
    void checkNicknameIsDuplicated() throws Exception {
        //given
        String nickname = "여행자#123";

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/check/nickname")
                                .param("nickname", nickname))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.isDuplicated").value(false));

        //docs
        DocumentGenerator.checkNickname(response);
    }

    @Test
    @DisplayName("[유저 API] 닉네임 중복확인 - 실패: 이미 사용 중인 닉네임")
    void checkNicknameIsDuplicated_fail_already_exist() throws Exception {
        //given
        String duplicatedNickname = "중복여행자";
        Scenario.createUser().nickname(duplicatedNickname).request();

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/check/nickname")
                                .param("nickname", duplicatedNickname))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.isDuplicated").value(true));
    }

    @Test
    @DisplayName("[유저 API] - 다른 유저 페이지 정보 조회하기")
    void getInfoOtherUserPage() throws Exception {
        //given
        final long targetUserId = 2;
        final String accessToken = Scenario.createUser().request().advance()
                .createAccessToken().getAccessToken();
        Scenario.createUser()
                .id(targetUserId)
                .nickname("nickname2")
                .providerId("providerId2")
                .request();

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/{userId}", targetUserId)
                                .header(AUTHORIZATION_HEADER, accessToken))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.id").value(targetUserId));

//                .andDo(document("user",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        pathParameters(
//                                parameterWithName("userId").description("(선택) 조회하려는 유저의 id. 만약 본인의 유저 페이지를 조회 한다면 빈 값 사용")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 식별값(id)"),
//                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("프로필 이미지 링크"),
//                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("접속자가 해당 페이지의 주인인지 확인"),
//                                fieldWithPath("data.isBlocked").type(JsonFieldType.BOOLEAN).description("해당 유저 차단 여부"),
//                                fieldWithPath("data.isIgnored").type(JsonFieldType.BOOLEAN).description("해당 유저 무시 여부")
//                        )
//                ));
    }

    @Test
    @DisplayName("[유저 API] - 본인 유저 페이지 정보 조회하기")
    void getInfoMyPage() throws Exception {
        //given
        final long myId = 1L;
        final String accessToken = Scenario.createUser().request().advance()
                .createAccessToken().getAccessToken();

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/")
                                .header(AUTHORIZATION_HEADER, accessToken))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.id").value(myId));

//                .andDo(document("user/me",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 식별값(id)"),
//                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("프로필 이미지 링크"),
//                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("접속자가 해당 페이지의 주인인지 확인 (항상 true)"),
//                                fieldWithPath("data.isBlocked").type(JsonFieldType.BOOLEAN).description("해당 유저 차단 여부 (항상 false)"),
//                                fieldWithPath("data.isIgnored").type(JsonFieldType.BOOLEAN).description("해당 유저 무시 여부 (항상 false)")
//                        )
//                ));
    }

    private static KakaoUserInformation mockKakaoUserInformation(final String providerId) {
        final String connectedAt = "connectedAt";
        final String nickname = "nickname";
        final String profileImage = "profileImage";
        final String thumbnailImage = "thumbnailImage";
        final String kakaoAccount = "kakaoAccount";
        return new KakaoUserInformation(
                providerId,
                connectedAt,
                nickname,
                profileImage,
                thumbnailImage,
                kakaoAccount
        );
    }


}