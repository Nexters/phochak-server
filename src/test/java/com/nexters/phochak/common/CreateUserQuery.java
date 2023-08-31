package com.nexters.phochak.common;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.domain.OAuthProviderEnum;

import java.time.LocalDateTime;

//@Component
public class CreateUserQuery {
    private long id = 1L;
    private FcmDeviceTokenEntity fcmDeviceToken = null;
    private OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;
    private String providerId = "providerId";
    private String nickname = "nickname";
    private String profileImgUrl = "profileImgUrl";
    private boolean isBlocked = false;
    private LocalDateTime leaveDate = null;

    public CreateUserQuery id(final long id) {
        this.id = id;
        return this;
    }

    public CreateUserQuery fcmDeviceToken(final FcmDeviceTokenEntity fcmDeviceToken) {
        this.fcmDeviceToken = fcmDeviceToken;
        return this;
    }

    public CreateUserQuery provider(final OAuthProviderEnum provider) {
        this.provider = provider;
        return this;
    }

    public CreateUserQuery providerId(final String providerId) {
        this.providerId = providerId;
        return this;
    }

    public CreateUserQuery nickname(final String nickname) {
        this.nickname = nickname;
        return this;
    }

    public CreateUserQuery profileImgUrl(final String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
        return this;
    }

    public CreateUserQuery isBlocked(final boolean isBlocked) {
        this.isBlocked = isBlocked;
        return this;
    }

    public CreateUserQuery leaveDate(final LocalDateTime leaveDate) {
        this.leaveDate = leaveDate;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        UserEntity user = new UserEntity(
                id,
                fcmDeviceToken,
                provider,
                providerId,
                nickname,
                profileImgUrl,
                isBlocked,
                leaveDate
        );
        TestUtil.userRepository.save(user);
        return new Scenario.NextScenarioStep(null);
    }

}
