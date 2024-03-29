package com.nexters.phochak.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.DocumentGenerator;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.user.adapter.in.web.UserController;
import com.nexters.phochak.user.adapter.out.api.KakaoInformationFeignClient;
import com.nexters.phochak.user.adapter.out.persistence.IgnoredUserEntityRelation;
import com.nexters.phochak.user.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
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
import static com.nexters.phochak.common.TestUtil.TestUser.accessToken;
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
    @Autowired
    IgnoredUserRepository ignoredUserRepository;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    KakaoInformationFeignClient kakaoInformationFeignClient;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userController).build();
        TestUtil.setMockMvc(mockMvc);
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

        //doc
        DocumentGenerator.login(response);
    }

    @Test
    @DisplayName("[유저 API] - 닉네임 변경")
    void modifyNickname() throws Exception {
        //given
        final String newNickname = "새로운_여행자";
        NicknameModifyRequestDto requestDto = new NicknameModifyRequestDto(newNickname);

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .put("/v1/user/nickname")
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        assertThat(userRepository.findById(1L).get().getNickname()).isEqualTo(newNickname);

        //doc
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

        //doc
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
    void getOtherUserInfo() throws Exception {
        //given
        final long targetUserId = 2;
        Scenario.createUser().id(targetUserId).request();

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/{userId}", targetUserId)
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.id").value(targetUserId));

        //doc
        DocumentGenerator.getOtherUserInfo(response);
    }

    @Test
    @DisplayName("[유저 API] - 본인 유저 페이지 정보 조회하기")
    void getInfoMyPage() throws Exception {
        //given
        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/")
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.id").value(TestUtil.TestUser.userId));

        //doc
        DocumentGenerator.getMyUserInfo(response);
    }

    @Test
    @DisplayName("[유저 무시 API] - 유저 무시하기")
    void ignoreUser() throws Exception {
        //given
        final long ignoredUserId = 2L;
        Scenario.createUser().id(ignoredUserId).request();

        //when
        final ResultActions response = Scenario.ignoreUser().request().getResponse();

        //then
        final UserEntity userEntity = userRepository.getReferenceById(TestUtil.TestUser.userId);
        final UserEntity ignoredUserEntity = userRepository.getReferenceById(ignoredUserId);
        final IgnoredUserEntityRelation pk = new IgnoredUserEntityRelation(userEntity, ignoredUserEntity);
        assertThat(ignoredUserRepository.existsByIgnoredUserRelation(pk)).isTrue();

        //doc
        DocumentGenerator.ignoreUser(response);
    }

    @Test
    @DisplayName("[유저 무시 API] - 유저 무시하기 취소")
    void cancelIgnoreUser() throws Exception {
        //given
        final long ignoredUserId = 2L;
        Scenario.createUser().id(ignoredUserId).request()
                .advance().ignoreUser().request();

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .delete("/v1/user/ignore/{ignoredUserId}", ignoredUserId)
                                .header(AUTHORIZATION_HEADER, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        final UserEntity userEntity = userRepository.getReferenceById(TestUtil.TestUser.userId);
        final UserEntity ignoredUserEntity = userRepository.getReferenceById(ignoredUserId);
        final IgnoredUserEntityRelation pk = new IgnoredUserEntityRelation(userEntity, ignoredUserEntity);
        assertThat(ignoredUserRepository.existsByIgnoredUserRelation(pk)).isFalse();

        //doc
        DocumentGenerator.cancelIgnoreUser(response);
    }

    @Test
    @DisplayName("[유저 무시 API] - 무시하기한 유저 목록 조회")
    void getIgnoreUser() throws Exception {
        //given
        Scenario.createUser().id(2L).request()
                .advance().ignoreUser().ignoredUserId(2L).request()
                .advance().createUser().id(3L).request()
                .advance().ignoreUser().ignoredUserId(3L).request()
                .advance().createUser().id(4L).request();
        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/ignore", 10)
                                .header(AUTHORIZATION_HEADER, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.length()").value(2));

        //doc
        DocumentGenerator.getIgnoredUser(response);
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
