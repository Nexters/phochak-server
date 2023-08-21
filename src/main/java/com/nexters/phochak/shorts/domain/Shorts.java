package com.nexters.phochak.shorts.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Shorts {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @JsonProperty("state")
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
