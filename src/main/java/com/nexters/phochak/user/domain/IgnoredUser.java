package com.nexters.phochak.user.domain;

import lombok.Getter;

@Getter
public class IgnoredUser {
    private final User user;
    private final User ignoredUser;

    public IgnoredUser(final User user, final User ignoredUser) {
        this.user = user;
        this.ignoredUser = ignoredUser;
    }

}
