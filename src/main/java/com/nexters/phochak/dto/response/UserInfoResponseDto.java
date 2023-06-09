package com.nexters.phochak.dto.response;

import com.nexters.phochak.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserInfoResponseDto {
    private long id;
    private String nickname;
    private String profileImgUrl;
    private Boolean isMyPage;
    private Boolean isIgnored;
    private Boolean isBlocked;

    public static UserInfoResponseDto of(User user, Boolean isMyPage, Boolean isIgnored) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImgUrl(user.getProfileImgUrl())
                .isMyPage(isMyPage)
                .isIgnored(isIgnored)
                .isBlocked(user.getIsBlocked())
                .build();
    }
}
