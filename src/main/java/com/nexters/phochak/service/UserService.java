package com.nexters.phochak.service;

import com.nexters.phochak.dto.response.IgnoredUserResponseDto;
import com.nexters.phochak.dto.response.UserCheckResponseDto;
import com.nexters.phochak.dto.response.UserInfoResponseDto;

import java.util.List;

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

    /**
     * 원하는 닉네임으로 닉네임을 변경한다.
     * @param nickname
     */
    void modifyNickname(String nickname);

    /**
     * 해당 유저의 정보를 조회한다.
     * @param userId
     * @return
     */
    UserInfoResponseDto getInfo(Long pageOwnerId, Long userId);

    void withdraw(Long userId);

    void ignoreUser(Long me, Long ignoredUserId);

    void cancelIgnoreUser(Long me, Long ignoredUserId);

    List<IgnoredUserResponseDto> getIgnoreUserList(Long me);
}
