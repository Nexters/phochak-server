package com.nexters.phochak.service;

public interface UserService {

    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param code
     */
    Long login(String provider, String code);

    /**
     * 해당 id의 유저 정보를 조회한다.
     */
    void validateUser(Long userId);
}
