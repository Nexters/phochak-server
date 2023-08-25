package com.nexters.phochak.likes.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class QuerydslFetchDto {

    private boolean isLiked;

    @QueryProjection
    public QuerydslFetchDto(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
