package com.nexters.phochak.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

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
