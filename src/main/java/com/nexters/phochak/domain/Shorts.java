package com.nexters.phochak.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class Shorts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortsUrl;

    @Column(nullable = false, unique = true)
    private String thumbnailUrl;

    public Shorts() {
    }

    @Builder
    public Shorts(String shortsUrl, String thumbnailUrl) {
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
