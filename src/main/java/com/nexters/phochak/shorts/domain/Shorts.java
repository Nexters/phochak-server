package com.nexters.phochak.shorts.domain;

public class Shorts {

    private Long id;

    private ShortsStateEnum shortsStateEnum;

    private String uploadKey;

    private String shortsUrl;

    private String thumbnailUrl;

    public Shorts(final ShortsStateEnum shortsStateEnum, final String uploadKey, final String shortsUrl, final String thumbnailUrl) {
        this.shortsStateEnum = shortsStateEnum;
        this.uploadKey = uploadKey;
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void updateShortsState(final ShortsStateEnum shortsStateEnum) {
        this.shortsStateEnum = shortsStateEnum;
    }
}
