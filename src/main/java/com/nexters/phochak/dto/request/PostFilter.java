package com.nexters.phochak.dto.request;

import lombok.Getter;

@Getter
public enum PostFilter {
    UPLOADED,  // 내가 업로드한 동영상
    LIKED; // 내가 좋아요한 동영상
}
