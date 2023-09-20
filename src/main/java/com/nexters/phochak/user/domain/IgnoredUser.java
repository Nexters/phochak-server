package com.nexters.phochak.user.domain;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class IgnoredUser {
    private final User user;
    private final User ignoredUser;

    public IgnoredUser(final User user, final User ignoredUser) {
        validateConstructor(user, ignoredUser);
        this.user = user;
        this.ignoredUser = ignoredUser;
    }

    private static void validateConstructor(final User user, final User ignoredUser) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(ignoredUser, "ignoredUser must not be null");
    }

}
