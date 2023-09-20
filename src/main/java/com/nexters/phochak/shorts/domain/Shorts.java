package com.nexters.phochak.shorts.domain;

import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class Shorts {

    private Long id;

    private ShortsStateEnum shortsStateEnum;

    private String uploadKey;

    private String shortsUrl;

    private String thumbnailUrl;

    public Shorts(final ShortsStateEnum shortsStateEnum, final String uploadKey, final String shortsUrl, final String thumbnailUrl) {
        validateConstructor(shortsStateEnum, uploadKey, shortsUrl, thumbnailUrl);
        this.shortsStateEnum = shortsStateEnum;
        this.uploadKey = uploadKey;
        this.shortsUrl = shortsUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    private static void validateConstructor(final ShortsStateEnum shortsStateEnum, final String uploadKey, final String shortsUrl, final String thumbnailUrl) {
        Assert.notNull(shortsStateEnum, "shortsStateEnum must not be null");
        Assert.notNull(uploadKey, "uploadKey must not be null");
        Assert.notNull(shortsUrl, "shortsUrl must not be null");
        Assert.notNull(thumbnailUrl, "thumbnailUrl must not be null");
    }

    public void assignId(final Long id) {
        this.id = id;
    }

    public void updateShortsState(final ShortsStateEnum shortsStateEnum) {
        this.shortsStateEnum = shortsStateEnum;
    }

    public void successEncoding() {
        this.shortsStateEnum = ShortsStateEnum.OK;
    }

    public void failEncoding() {
        this.shortsStateEnum = ShortsStateEnum.FAIL;
    }
}
