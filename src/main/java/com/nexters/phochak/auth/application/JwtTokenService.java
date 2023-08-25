package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.presentation.JwtResponseDto;
import com.nexters.phochak.auth.presentation.ReissueTokenRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface JwtTokenService {
    @AllArgsConstructor
    @Getter
    class TokenVo {
        public static final String TOKEN_TYPE = "Bearer";
        private String tokenString;
        private String expiresIn;
    }

    /**
     * 로그인 처리를 위한 토큰을 담은 응답 정보를 만든다
     * @param userId
     * @return
     */
    JwtResponseDto issueToken(Long userId);

    /**
     * 특정 token의 유효성을 검증한다.
     *
     * @param token
     * @return
     */
    Long validateJwt(String token);

    /**
     * JWT 형태의 Token을 생성한다.
     * @param userId
     * @param expireLength
     * @return
     */
    TokenVo generateToken(Long userId, Long expireLength);

    /**
     * AT, RT 를 재발급한다.
     * @param reissueTokenRequestDto
     * @return
     */
    JwtResponseDto reissueToken(ReissueTokenRequestDto reissueTokenRequestDto);

    /**
     * 로그아웃.
     * @param refreshToken
     */
    void logout(String refreshToken);
}
