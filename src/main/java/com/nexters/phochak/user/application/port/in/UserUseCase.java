package com.nexters.phochak.user.application.port.in;

public interface UserUseCase {
    /**
     * OAuth 로그인을 진행한다.
     *
     * @param provider
     * @param requestDto
     */
    Long login(String provider, LoginRequestDto requestDto);

    /**
     * 해당 닉네임이 중복되었는지 확인한다.
     * @param nickname
     * @return
     */
    UserCheckResponseDto checkNicknameIsDuplicated(String nickname);

    /**
     * 원하는 닉네임으로 닉네임을 변경한다.
     *
     * @param userId
     * @param nickname
     */
    void modifyNickname(final Long userId, String nickname);

    /**
     * 해당 유저의 정보를 조회한다.
     * @param userId
     * @return
     */
    UserInfoResponseDto getInfo(Long userId, Long pageOwnerId);

    void withdraw(Long userId);
}
