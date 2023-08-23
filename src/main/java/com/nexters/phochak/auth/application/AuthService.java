package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.presentation.LoginRequestDto;

public interface AuthService {
    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param requestDto
     */
    Long login(String provider, LoginRequestDto requestDto);
}
