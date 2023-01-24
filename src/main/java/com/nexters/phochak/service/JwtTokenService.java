package com.nexters.phochak.service;

import com.nexters.phochak.dto.response.LoginResponseDto;

public interface JwtTokenService {
    /**
     * 로그인 처리를 위한 토큰을 담은 응답 정보를 만든다
     * @param userId
     * @return
     */
    LoginResponseDto createLoginResponse(Long userId);
}
