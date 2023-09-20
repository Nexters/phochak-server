package com.nexters.phochak.notification.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class FcmDeviceToken {
    private Long id;
    private final User user;
    private final String token;
    private final OperatingSystem operatingSystem;

    public FcmDeviceToken(final User user, final String token, final OperatingSystem operatingSystem) {
        validateConstructor(user, token, operatingSystem);
        this.user = user;
        this.token = token;
        this.operatingSystem = operatingSystem;
    }

    private static void validateConstructor(final User user, final String token, final OperatingSystem operatingSystem) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(token, "token must not be null");
        Assert.notNull(operatingSystem, "operatingSystem must not be null");
    }

    public void assignId(final Long id) {
        this.id = id;
    }
}
