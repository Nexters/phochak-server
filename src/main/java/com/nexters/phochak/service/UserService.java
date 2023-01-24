package com.nexters.phochak.service;

import com.nexters.phochak.dto.response.LoginResponseDto;

public interface UserService {

    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param code
     */
    LoginResponseDto login(String provider, String code);
}
