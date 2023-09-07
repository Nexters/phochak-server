package com.nexters.phochak.post.application.port.in;

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
