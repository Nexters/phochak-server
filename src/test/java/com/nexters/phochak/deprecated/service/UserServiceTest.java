package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.UserCheckResponseDto;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.UserServiceImpl;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.port.in.KakaoUserInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.nexters.phochak.user.application.port.in.KakaoUserInformation.KakaoOAuthProperties;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Disabled
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    MockUserEntity user = new MockUserEntity();
    KakaoOAuthProperties kakaoOAuthProperties;
    KakaoUserInformation userInformation;
    JwtTokenUseCase.TokenVo tokenVo;


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

        tokenVo = new JwtTokenUseCase.TokenVo(tokenString, expiresIn);
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

    static class MockUserEntity extends UserEntity {
        @Override
        public Long getId() {
            return 1L;
        }
    }
}
