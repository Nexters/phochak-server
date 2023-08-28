package com.nexters.phochak.user.domain;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceToken;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.assertj.core.util.VisibleForTesting;

import java.time.LocalDateTime;

public class UserFixture {

    private Long userId = 1L;
    private FcmDeviceToken fcmToken = null;
    private OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;
    private String providerId = "providerId";
    private String nickname = "nickname";
    private String profileImgUrl = "profileImgUrl";
    private boolean isBlocked = false;
    private LocalDateTime leaveDate = null;

    public static UserFixture anUser() {
        return new UserFixture();
    }

    public UserFixture userId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public UserFixture fcmToken(final FcmDeviceToken fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public UserFixture provider(final OAuthProviderEnum provider) {
        this.provider = provider;
        return this;
    }

    public UserFixture providerId(final String providerId) {
        this.providerId = providerId;
        return this;
    }

    public UserFixture nickname(final String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserFixture profileImgUrl(final String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
        return this;
    }

    public UserFixture isBlocked(final boolean isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }

    public UserFixture leaveDate(final LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
        return this;
    }

    @VisibleForTesting
    public UserEntity build() {
        return new UserEntity(
                userId,
                fcmToken,
                provider,
                providerId,
                nickname,
                profileImgUrl,
                isBlocked,
                leaveDate
        );
    }
}
