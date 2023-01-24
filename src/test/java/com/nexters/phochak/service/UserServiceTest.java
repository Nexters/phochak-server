package com.nexters.phochak.service;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.KakaoUserInformation;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.service.impl.KakaoOAuthServiceImpl;
import com.nexters.phochak.service.impl.UserServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.nexters.phochak.dto.KakaoUserInformation.KakaoOAuthProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    JwtTokenServiceImpl jwtTokenService;
    @Mock
    UserRepository userRepository;
    @Mock
    Map<OAuthProviderEnum, OAuthService> oAuthServiceMap;
    @Mock
    KakaoOAuthServiceImpl kakaoOAuthService;
    @InjectMocks
    UserServiceImpl userService;

    MockUser user = new MockUser();
    OAuthProviderEnum providerEnum = OAuthProviderEnum.KAKAO;
    KakaoOAuthProperties kakaoOAuthProperties;
    KakaoUserInformation userInformation;
    TokenDto tokenDto;


    @BeforeEach
    void setUp() {
        String providerId = "testProviderId";
        String kakaoNickname = "seyeong";
        String tokenString = "testToken";
        String expiresIn = "expires";

        kakaoOAuthProperties = KakaoOAuthProperties.builder()
                .nickname(kakaoNickname)
                .build();

        userInformation = KakaoUserInformation.builder()
                .providerId(providerId)
                .properties(kakaoOAuthProperties)
                .build();

        tokenDto = new TokenDto(tokenString, expiresIn);
    }

    @Test
    @DisplayName("로그인 시 신규 회원이면 회원가입이 호출된다")
    void login_newUser() {
        // given
        String code = "testCode";
        String providerId = "testProviderId";

        given(oAuthServiceMap.get(providerEnum)).willReturn(kakaoOAuthService);
        given(kakaoOAuthService.requestUserInformation(code)).willReturn(userInformation);
        given(userRepository.findByProviderAndProviderId(providerEnum, providerId)).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(user);
        given(jwtTokenService.generateAccessToken(any())).willReturn(tokenDto);

        // when
        userService.login("kakao", code);

        // then
        then(userRepository).should(atLeastOnce()).save(any());
        assertThat(user.getNickname()).startsWith("여행자 seyeong");
    }

    @Test
    @DisplayName("로그인 시 기존 회원이면 회원가입이 호출되지 않는다")
    void login_alreadyUser() {
        // given
        String code = "testCode";
        String providerId = "testProviderId";

        given(oAuthServiceMap.get(providerEnum)).willReturn(kakaoOAuthService);
        given(kakaoOAuthService.requestUserInformation(code)).willReturn(userInformation);
        given(userRepository.findByProviderAndProviderId(providerEnum, providerId)).willReturn(Optional.of(user));
        given(jwtTokenService.generateAccessToken(any())).willReturn(tokenDto);

        // when
        userService.login("kakao", code);

        // then
        then(userRepository).should(never()).save(any());
    }

    static class MockUser extends User {
        @Override
        public Long getId() {
            return 1L;
        }
    }
}
