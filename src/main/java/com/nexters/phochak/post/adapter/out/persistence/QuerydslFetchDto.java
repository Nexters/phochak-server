package com.nexters.phochak.post.adapter.out.persistence;

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
