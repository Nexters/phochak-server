package com.nexters.phochak.service;

public interface UserService {

    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param code
     */
    void login(String provider, String code);
}
