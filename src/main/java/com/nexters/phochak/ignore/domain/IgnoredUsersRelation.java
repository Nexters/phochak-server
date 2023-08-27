package com.nexters.phochak.ignore.domain;

import com.nexters.phochak.user.domain.UserEntity;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
public class IgnoredUsersRelation implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", referencedColumnName = "USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IGNORED_USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity ignoredUser;

    @Builder
    public IgnoredUsersRelation(UserEntity user, UserEntity ignoredUser) {
        this.user = user;
        this.ignoredUser = ignoredUser;
    }
}
