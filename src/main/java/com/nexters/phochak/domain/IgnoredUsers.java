package com.nexters.phochak.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Getter
@Entity
@Table(indexes = {@Index(name="idx02_unique_ignored_user", columnList = "USER_ID, IGNORED_USER_ID", unique = true)})
public class IgnoredUsers {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="USER_ID", referencedColumnName = "USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IGNORED_USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User ignoredUser;

    @Builder
    public IgnoredUsers(User user, User ignoredUser) {
        this.user = user;
        this.ignoredUser = ignoredUser;
    }

    public IgnoredUsers() {
    }
}
