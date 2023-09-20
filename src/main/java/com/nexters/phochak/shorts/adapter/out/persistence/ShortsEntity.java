package com.nexters.phochak.shorts.adapter.out.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name="SHORTS")
public class ShortsEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SHORTS_ID")
    private Long id;

    @JsonProperty("state")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShortsStateEnum shortsStateEnum;

    @Column(nullable = false, unique = true)
    private String uploadKey;

    @Column(nullable = false, unique = true)
    private String shortsUrl;

    @Column(nullable = false, unique = true)
    private String thumbnailUrl;

    public ShortsEntity() {
    }

    public ShortsEntity(Long id, ShortsStateEnum shortsStateEnum, String uploadKey, String shortsUrl, String thumbnailUrl) {
        this.id = id;
        this.shortsStateEnum = shortsStateEnum;
        this.uploadKey = uploadKey;
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
