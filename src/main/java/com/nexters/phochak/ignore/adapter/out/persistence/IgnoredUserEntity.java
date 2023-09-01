package com.nexters.phochak.ignore.adapter.out.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity(name = "ignored_user")
public class IgnoredUserEntity {

    @EmbeddedId
    private IgnoredUserEntityRelation ignoredUserRelation;

    @Builder
    public IgnoredUserEntity(IgnoredUserEntityRelation ignoredUserRelation) {
        this.ignoredUserRelation = ignoredUserRelation;
    }

    public IgnoredUserEntity() {
    }
}
