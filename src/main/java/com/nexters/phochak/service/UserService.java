package com.nexters.phochak.service;

import com.nexters.phochak.dto.response.UserCheckResponseDto;

public interface UserService {

    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param token
     */
    Long login(String provider, String token);

    /**
     * 해당 id의 유저 정보를 조회한다.
     * @param userId
     */
    void validateUser(Long userId);

    /**
     * 해당 닉네임이 중복되었는지 확인한다.
     * @param nickname
     * @return
     */
    UserCheckResponseDto checkNicknameIsDuplicated(String nickname);
}
