package com.nexters.phochak.user.domain;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private final FcmDeviceTokenEntity fcmDeviceTokenEntity;
    private final OAuthProviderEnum provider;
    private final String providerId;
    private String nickname;
    private final String profileImgUrl;
    private final Boolean isBlocked;
    private final LocalDateTime leaveDate;

    public User(final FcmDeviceTokenEntity fcmDeviceTokenEntity, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.fcmDeviceTokenEntity = fcmDeviceTokenEntity;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isBlocked = isBlocked;
        this.leaveDate = leaveDate;
    }

    public User(final Long id, final FcmDeviceTokenEntity fcmDeviceTokenEntity, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.id = id;
        this.fcmDeviceTokenEntity = fcmDeviceTokenEntity;
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

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

}
