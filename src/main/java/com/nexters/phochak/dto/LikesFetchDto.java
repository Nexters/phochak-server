package com.nexters.phochak.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LikesFetchDto {

    private int like;
    private boolean isLiked;

    public LikesFetchDto(int like, boolean isLiked) {
        this.like = like;
        this.isLiked = isLiked;
    }
}
