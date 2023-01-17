package com.nexters.phochak.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class Phochak extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="PHOCHAK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public Phochak() {
    }

    @Builder
    public Phochak(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
