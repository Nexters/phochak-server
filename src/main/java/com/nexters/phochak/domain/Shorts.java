package com.nexters.phochak.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class Shorts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="POST_ID", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    private String videoUrl;

    private String extension;

    private Long length;

    public Shorts() {
    }

    @Builder
    public Shorts(Post post, String videoUrl, String extension, Long length) {
        this.post = post;
        this.videoUrl = videoUrl;
        this.extension = extension;
        this.length = length;
    }
}
