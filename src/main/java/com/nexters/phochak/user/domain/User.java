package com.nexters.phochak.user.domain;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private FcmDeviceTokenEntity fcmDeviceTokenEntity;
    private OAuthProviderEnum provider;
    private String providerId;
    private String nickname;
    private String profileImgUrl;
    private Boolean isBlocked;
    private LocalDateTime leaveDate;

    public User(final FcmDeviceTokenEntity fcmDeviceTokenEntity, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
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

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void withdraw() {
        this.nickname = null;
        this.providerId = null;
        this.provider = null;
        this.profileImgUrl = null;
        this.leaveDate = LocalDateTime.now();
    }

    public void modifyNickname(final String nickname) {
        this.nickname = nickname;
    }
}
