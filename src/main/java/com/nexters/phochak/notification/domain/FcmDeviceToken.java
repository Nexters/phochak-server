package com.nexters.phochak.notification.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;

@Getter
public class FcmDeviceToken {
    private Long id;
    private final User user;
    private final String token;
    private final OperatingSystem operatingSystem;

    public FcmDeviceToken(final User user, final String token, final OperatingSystem operatingSystem) {
        this.user = user;
        this.token = token;
        this.operatingSystem = operatingSystem;
    }

    public void assignId(final Long id) {
        this.id = id;
    }
}
