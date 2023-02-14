package com.nexters.phochak.service;

import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.request.ReissueAccessTokenRequestDto;
import com.nexters.phochak.dto.response.JwtResponseDto;

public interface JwtTokenService {
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
     * Access Token 하나를 발급한다.
     * @param userId
     * @return
     */
    TokenDto generateAccessToken(Long userId);

    JwtResponseDto reissueAccessToken(ReissueAccessTokenRequestDto reissueAccessTokenRequestDto);
}
