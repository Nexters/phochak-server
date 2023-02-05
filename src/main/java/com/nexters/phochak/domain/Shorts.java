package com.nexters.phochak.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Shorts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false, unique = true)
    private String shortsUrl;

    @Column(nullable = false, unique = true)
    private String thumbnailUrl;

    public Shorts() {
    }

    @Builder
    public Shorts(Long id, String shortsUrl, String thumbnailUrl) {
        this.id = id;
        this.key = key;
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
