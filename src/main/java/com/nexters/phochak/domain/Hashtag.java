package com.nexters.phochak.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "idx_hashtag", columnList = "tag"))
public class Hashtag {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="TAG_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    private String tag;

    public Hashtag() {
    }

    @Builder
    public Hashtag(Post post, String tag) {
        this.post = post;
        this.tag = tag;
    }
}
