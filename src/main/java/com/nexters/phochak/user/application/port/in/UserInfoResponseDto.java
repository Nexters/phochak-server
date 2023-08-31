package com.nexters.phochak.user.application.port.in;

import com.nexters.phochak.user.domain.User;

public record UserInfoResponseDto(
        long id,
        String nickname,
        String profileImgUrl,
        Boolean isMyPage,
        Boolean isIgnored,
        Boolean isBlocked) {

    public static UserInfoResponseDto of(User user, Boolean isMyPage, Boolean isIgnored) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getNickname(),
                user.getProfileImgUrl(),
                isMyPage,
                isIgnored,
                user.getIsBlocked());
    }
}
