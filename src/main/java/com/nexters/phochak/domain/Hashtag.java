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
import javax.validation.constraints.Size;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_hashtag", columnList = "tag"))
public class Hashtag {
    public static final int HASHTAG_MAX_SIZE = 20;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @Size(min = 1, max = HASHTAG_MAX_SIZE)
    private String tag;

    public Hashtag() {
    }

    @Builder
    public Hashtag(Post post, String tag) {
        this.post = post;
        this.tag = tag;
    }
}
