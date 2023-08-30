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
        final OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;
        final String providerId = "newProviderId";
        final KakaoUserInformation kakaoRequestResponse = mockKakaoUserInformation(providerId);
        when(kakaoInformationFeignClient.call(any(), any())).thenReturn(kakaoRequestResponse);

        final ResultActions response = Scenario.login().request().getResponse();
        DocumentGenerator.login(response);

        assertThat(userRepository.findByProviderAndProviderId(provider, providerId).isPresent()).isTrue();
    }

    @Test
    @DisplayName("[유저 API] - 닉네임 변경")
    void modifyNickname() throws Exception {
        Scenario.createUser().request();

        NicknameModifyRequestDto requestDto = new NicknameModifyRequestDto("새로운 여행자");
        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .put("/v1/user/nickname")
                                .header(AUTHORIZATION_HEADER, "access token")
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andDo(document("user/modify/nickname",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("nickname").description("(필수) 변경하고자 하는 닉네임")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
//                        )
//                ));
    }

    @Test
    @DisplayName("[유저 API] 닉네임 중복확인")
    void checkNicknameIsDuplicated() throws Exception {
        String nickname = "여행자#123";
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/check/nickname")
                                .param("nickname", nickname))
                .andExpect(status().isOk());
        DocumentGenerator.checkNickname(response);
        response.andExpect(jsonPath("$.data.isDuplicated").value(false));
    }

    //TODO: 닉네임 변경 리팩토링 이후 추가
    @Test
    @DisplayName("[유저 API] 닉네임 중복확인 - 실패: 이미 사용 중인 닉네임")
    void checkNicknameIsDuplicated_fail_already_exist() throws Exception {
        String duplicatedNickname = "중복여행자";
        Scenario.createUser().nickname(duplicatedNickname).request();
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/check/nickname")
                                .param("nickname", duplicatedNickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isDuplicated").value(true));
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