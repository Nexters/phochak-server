package com.nexters.phochak.user.application.port.in;

public interface AuthUseCase {
    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param requestDto
     */
    Long login(String provider, LoginRequestDto requestDto);
}
