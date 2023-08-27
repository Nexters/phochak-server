package com.nexters.phochak.likes.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(indexes =
        {@Index(name = "idx01_likes", columnList = "post_id"),
        @Index(name="idx02_unique_likes", columnList = "USER_ID, POST_ID", unique = true)})
public class Likes extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="LIKES_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    public Likes() {
    }

    @Builder
    public Likes(UserEntity user, Post post) {
        this.user = user;
        this.post = post;
    }

    public boolean hasLikesByUser(Long userId) {
        return Objects.equals(this.user.getId(), userId);
    }
}
