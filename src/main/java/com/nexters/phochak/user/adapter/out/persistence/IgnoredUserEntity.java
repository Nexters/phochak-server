package com.nexters.phochak.user.adapter.out.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity(name = "ignored_user")
public class IgnoredUserEntity {

    @EmbeddedId
    private IgnoredUserEntityRelation ignoredUserRelation;

    public IgnoredUserEntity(IgnoredUserEntityRelation ignoredUserRelation) {
        this.ignoredUserRelation = ignoredUserRelation;
    }

    public IgnoredUserEntity() {
    }
}
