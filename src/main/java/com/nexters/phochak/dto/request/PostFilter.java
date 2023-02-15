package com.nexters.phochak.dto.request;

import lombok.Getter;

@Getter
public enum PostFilter {
    UPLOADED; // 내가 업로드한 동영상

    public boolean isUploadedFilter() {
        return this == PostFilter.UPLOADED;
    }
}
