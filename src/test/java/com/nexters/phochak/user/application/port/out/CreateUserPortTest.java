package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.auth.application.port.in.KakaoUserInformation;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CreateUserPortTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    CreateUserPort createUserPort;
    KakaoUserInformation.KakaoOAuthProperties kakaoOAuthProperties;
    KakaoUserInformation userInformation;

    @BeforeEach
    void setUp() {
        String providerId = "providerId";
        String kakaoNickname = "kakaoNickname";

        kakaoOAuthProperties = KakaoUserInformation.KakaoOAuthProperties.builder()
                .nickname(kakaoNickname)
                .build();

        userInformation = KakaoUserInformation.builder()
                .providerId(providerId)
                .properties(kakaoOAuthProperties)
                .build();
    }

    @Test
    @DisplayName("로그인 시 신규 회원이면 회원가입이 호출된다")
    void login_newUser() {
        // given
        given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(Optional.empty());

        // when
        createUserPort.getOrCreateUser(userInformation);

        // then
        then(userRepository).should(atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("로그인 시 기존 회원이면 회원가입이 호출되지 않는다")
    void login_alreadyUser() {
        // given
        UserEntity userEntity = UserFixture.anUser().build();
        given(userRepository.findByProviderAndProviderId(any(), any())).willReturn(Optional.of(userEntity));

        // when
        createUserPort.getOrCreateUser(userInformation);

        // then
        then(userRepository).should(never()).save(any());
    }
}