package com.nexters.phochak.user.application.port.in;

import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
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

    public static UserInfoResponseDto of(UserEntity userEntity, Boolean isMyPage, Boolean isIgnored) {
        return UserInfoResponseDto.builder()
                .id(userEntity.getId())
                .nickname(userEntity.getNickname())
                .profileImgUrl(userEntity.getProfileImgUrl())
                .isMyPage(isMyPage)
                .isIgnored(isIgnored)
                .isBlocked(userEntity.getIsBlocked())
                .build();
    }
}
