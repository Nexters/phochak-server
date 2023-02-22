package com.nexters.phochak.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LikesFetchDto {

    private boolean isLiked;

    @QueryProjection
    public LikesFetchDto(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
