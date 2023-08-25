package com.nexters.phochak.auth.presentation;

import com.nexters.phochak.auth.adapter.in.web.AuthController;
import com.nexters.phochak.auth.adapter.out.web.KakaoInformationFeignClient;
import com.nexters.phochak.auth.application.port.in.KakaoUserInformation;
import com.nexters.phochak.common.DocumentGenerator;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class AuthControllerTest extends RestDocsApiTest {

    @Autowired
    AuthController authController;
    @Autowired UserRepository userRepository;
    @MockBean
    KakaoInformationFeignClient kakaoInformationFeignClient;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, authController).build();
    }

    @Test
    @DisplayName("인증 API - 카카오 OAuth 회원가입 성공")
    void sign_up() throws Exception {
        final OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;
        final String providerId = "newProviderId";
        final KakaoUserInformation kakaoRequestResponse = mockKakaoUserInformation(providerId);
        when(kakaoInformationFeignClient.call(any(), any())).thenReturn(kakaoRequestResponse);

        assertThat(userRepository.findByProviderAndProviderId(provider, providerId).isEmpty())
                .isTrue();

        final ResultActions response = Scenario.login().request(mockMvc).getResponse();
        DocumentGenerator.loginDocument(response);

        assertThat(userRepository.findByProviderAndProviderId(provider, providerId).isPresent())
                .isTrue();
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