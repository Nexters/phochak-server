package com.nexters.phochak.domain;

import com.nexters.phochak.specification.ShortsStateEnum;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Shorts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = false)
    private ShortsStateEnum shortsStateEnum;

    @Column(nullable = false, unique = true)
    private String uploadKey;

    @Column(nullable = false, unique = true)
    private String shortsUrl;

    @Column(nullable = false, unique = true)
    private String thumbnailUrl;

    public Shorts() {
    }

    @Builder
    public Shorts(Long id, String uploadKey, String shortsUrl, String thumbnailUrl) {
        this.id = id;
        this.shortsStateEnum = ShortsStateEnum.IN_PROGRESS;
        this.uploadKey = uploadKey;
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateShortsState(ShortsStateEnum shortsStateEnum) {
        this.shortsStateEnum = shortsStateEnum;
    }
}
