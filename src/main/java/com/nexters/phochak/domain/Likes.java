package com.nexters.phochak.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Entity
@Table(indexes = @Index(name="idx02_unique_likes", columnList = "USER_ID, POST_ID", unique = true))
public class Likes extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="LIKES_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public Likes() {
    }

    @Builder
    public Likes(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public boolean hasLikesByUser(Long userId) {
        return Objects.equals(this.user.getId(), userId);
    }
}
