package com.nexters.phochak.user.domain;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

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

    public static final int NICKNAME_MAX_SIZE = 10;

    public User(OAuthProviderEnum provider, String providerId, String nickname, String profileImgUrl) {
        validateConstructor(provider, providerId, nickname, profileImgUrl);
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }

    private static void validateConstructor(final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl) {
        Assert.notNull(provider, "provider must not be null");
        Assert.notNull(providerId, "providerId must not be null");
        Assert.notNull(nickname, "nickname must not be null");
        if (nickname.length() > 10) {
            throw new IllegalArgumentException("nickname must be less than 10 characters");
        }
        Assert.notNull(profileImgUrl, "profileImgUrl must not be null");
    }

    private User(final Long id, final FcmDeviceTokenEntity fcmDeviceTokenEntity, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.id = id;
        this.fcmDeviceTokenEntity = fcmDeviceTokenEntity;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isBlocked = isBlocked;
        this.leaveDate = leaveDate;
    }

    public static User forMapper(
            final Long id,
            final FcmDeviceTokenEntity fcmDeviceTokenEntity,
            final OAuthProviderEnum provider,
            final String providerId,
            final String nickname,
            final String profileImgUrl,
            final Boolean isBlocked,
            final LocalDateTime leaveDate) {
        return new User(id, fcmDeviceTokenEntity, provider, providerId, nickname, profileImgUrl, isBlocked, leaveDate);
    }

    public void assignId(Long generatedId) {
        this.id = generatedId;
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
