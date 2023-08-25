package com.nexters.phochak.auth.application.port.in;

public interface AuthProcessUseCase {
    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param requestDto
     */
    Long login(String provider, LoginRequestDto requestDto);
}
