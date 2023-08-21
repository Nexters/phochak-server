package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.auth.KakaoUserInformation;
import com.nexters.phochak.auth.TokenDto;
import com.nexters.phochak.auth.application.KakaoOAuthServiceImpl;
import com.nexters.phochak.auth.application.OAuthService;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.UserCheckResponseDto;
import com.nexters.phochak.user.application.UserServiceImpl;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import com.nexters.phochak.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static com.nexters.phochak.auth.KakaoUserInformation.KakaoOAuthProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@Deprecated
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
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

        // when
        userService.login("kakao", code);

        // then
        then(userRepository).should(atLeastOnce()).save(any());
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

        // when
        userService.login("kakao", code);

        // then
        then(userRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 NOT_FOUND_USER 예외가 발생한다")
    void validateUser() {
        // given
        given(userRepository.existsById(1L)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.validateUser(1L))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("닉네임 중복 체크 시 중복된 닉네임이 존재하면 true를 반환한다")
    void checkNickname_duplicated() {
        // given
        String nickname = "nickname";
        given(userRepository.existsByNickname(nickname)).willReturn(true);

        // when
        UserCheckResponseDto response = userService.checkNicknameIsDuplicated(nickname);

        // then
        assertThat(response.getIsDuplicated()).isTrue();
    }

    @Test
    @DisplayName("닉네임 변경 시 중복된 닉네임이 존재하면 예외가 발생한다")
    void modifyNickname_duplicated() {
        // given
        String nickname = "nickname";
        given(userRepository.existsByNickname(nickname)).willReturn(true);
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.modifyNickname(nickname))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.DUPLICATED_NICKNAME.getMessage());
    }

    static class MockUser extends User {
        @Override
        public Long getId() {
            return 1L;
        }
    }
}
