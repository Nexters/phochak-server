package com.nexters.phochak.notification.domain;

import com.nexters.phochak.user.domain.User;

public class FcmDeviceToken {
    private Long id;
    private final User user;
    private final String token;
    public FcmDeviceToken(final User user, final String token) {
        this.user = user;
        this.token = token;
    }
    public void assignId(final Long id) {
        this.id = id;
    }
}
