package com.nexters.phochak.user.domain;

import com.nexters.phochak.user.adapter.out.persistence.IgnoredUserEntity;
import lombok.Getter;

@Getter
public class IgnoredUser {
    private final User user;
    private final User ignoredUser;

    public IgnoredUser(final User user, final User ignoredUser) {
        this.user = user;
        this.ignoredUser = ignoredUser;
    }

    public static IgnoredUser toDomain(final IgnoredUserEntity entity) {
        return new IgnoredUser(
                User.toDomain(entity.getIgnoredUserRelation().getUser()),
                User.toDomain(entity.getIgnoredUserRelation().getIgnoredUser())
        );
    }
}
