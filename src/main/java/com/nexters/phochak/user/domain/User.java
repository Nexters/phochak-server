package com.nexters.phochak.user.domain;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceToken;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {
    private Long id;
    private final FcmDeviceToken fcmDeviceToken;
    private final OAuthProviderEnum provider;
    private final String providerId;
    private final String nickname;
    private final String profileImgUrl;
    private final Boolean isBlocked;
    private final LocalDateTime leaveDate;

    public User(final FcmDeviceToken fcmDeviceToken, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.fcmDeviceToken = fcmDeviceToken;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isBlocked = isBlocked;
        this.leaveDate = leaveDate;
    }

    public User(final Long id, final FcmDeviceToken fcmDeviceToken, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.id = id;
        this.fcmDeviceToken = fcmDeviceToken;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isBlocked = isBlocked;
        this.leaveDate = leaveDate;
    }

    public void assignId(Long generatedId) {
        this.id = generatedId;
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getFcmDeviceToken(),
                userEntity.getProvider(),
                userEntity.getProviderId(),
                userEntity.getNickname(),
                userEntity.getProfileImgUrl(),
                userEntity.getIsBlocked(),
                userEntity.getLeaveDate());
    }
}
