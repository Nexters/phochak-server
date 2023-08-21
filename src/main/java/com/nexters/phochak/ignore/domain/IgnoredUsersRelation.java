package com.nexters.phochak.ignore.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ConstraintMode;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
public class IgnoredUsersRelation implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", referencedColumnName = "USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IGNORED_USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User ignoredUser;

    @Builder
    public IgnoredUsersRelation(User user, User ignoredUser) {
        this.user = user;
        this.ignoredUser = ignoredUser;
    }
}
