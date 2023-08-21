package com.nexters.phochak.ignore.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class IgnoredUsers {

    @EmbeddedId
    private IgnoredUsersRelation ignoredUsersRelation;

    @Builder
    public IgnoredUsers(IgnoredUsersRelation ignoredUsersRelation) {
        this.ignoredUsersRelation = ignoredUsersRelation;
    }

    public IgnoredUsers() {
    }
}
