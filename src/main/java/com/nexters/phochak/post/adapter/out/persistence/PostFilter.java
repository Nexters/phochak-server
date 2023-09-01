package com.nexters.phochak.post.adapter.out.persistence;

import lombok.Getter;

@Getter
public enum PostFilter {
    UPLOADED, // 내가 업로드한 동영상
    LIKED, // 내가 좋아요한 동영상
    SEARCH, // 게시글 검색
    NONE // 필터 없음 (메인 피드)
}
