package com.nexters.phochak.deprecated.auth.aspect;

import com.nexters.phochak.auth.application.JwtTokenServiceImpl;
import com.nexters.phochak.auth.aspect.AuthAspect;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.UserServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@Deprecated
@ExtendWith(MockitoExtension.class)
class AuthAspectTest {
    @Mock
    UserServiceImpl userService;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    ProceedingJoinPoint joinPoint;
    @Mock
    JwtTokenServiceImpl jwtTokenService;
    @InjectMocks
    AuthAspect aspect;

    @Test
    @DisplayName("토큰이 존재하지 않으면 TOKEN_NOT_FOUND 예외가 발생한다")
    void validateAccessToken_NoToken() {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("");

        // when & then
        Assertions.assertThatThrownBy(() -> aspect.validateAccessToken(joinPoint))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.TOKEN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("토큰 형식이 유효하지 않으면 INVALID_TOKEN 예외가 발생한다")
    void validateAccessToken_InvalidFormatToken() {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("BEARER Invalid Token");

        // when & then
        Assertions.assertThatThrownBy(() -> aspect.validateAccessToken(joinPoint))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("만료된 토큰이 들어오면 EXPIRED_TOKEN 예외가 발생한다")
    void validateAccessToken_ExpiredToken() {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("Bearer Valid Token");
        given(jwtTokenService.validateJwt("Valid Token")).willThrow(new PhochakException(ResCode.EXPIRED_TOKEN));

        // when & then
        Assertions.assertThatThrownBy(() -> aspect.validateAccessToken(joinPoint))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.EXPIRED_TOKEN.getMessage());
    }

    @Test
    @DisplayName("토큰 검증 시 예외가 발생하면 INVALID_TOKEN 예외가 발생한다")
    void validateAccessToken_TokenException() {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("Bearer Valid Token");
        given(jwtTokenService.validateJwt("Valid Token")).willThrow(new PhochakException(ResCode.INVALID_TOKEN));

        // when & then
        Assertions.assertThatThrownBy(() -> aspect.validateAccessToken(joinPoint))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("토큰의 유저 정보 검증에 실패하면 NOT_FOUND_USER 예외가 발생한다")
    void validateAccessToken_NotFoundUser() {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("Bearer Valid Token");
        given(jwtTokenService.validateJwt("Valid Token")).willReturn(1L);
        willThrow(new PhochakException(ResCode.NOT_FOUND_USER)).given(userService).validateUser(1L);

        // when & then
        Assertions.assertThatThrownBy(() -> aspect.validateAccessToken(joinPoint))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.NOT_FOUND_USER.getMessage());
    }

    @Test
    @DisplayName("유효한 토큰으로 유저 정보 검증에 성공한다")
    void validateAccessToken_success() throws Throwable {
        // given
        given(httpServletRequest.getHeader(anyString())).willReturn("Bearer Valid Token");
        given(jwtTokenService.validateJwt("Valid Token")).willReturn(1L);
        willDoNothing().given(userService).validateUser(1L);

        // when
        aspect.validateAccessToken(joinPoint);

        // then
        then(joinPoint).should(times(1)).proceed();
    }
}
