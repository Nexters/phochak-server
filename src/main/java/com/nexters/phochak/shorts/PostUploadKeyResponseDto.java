package com.nexters.phochak.shorts;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUploadKeyResponseDto {

    private final String uploadUrl;

    private final String uploadKey;

    @Builder
    public PostUploadKeyResponseDto(String uploadUrl, String uploadKey) {
        this.uploadUrl = uploadUrl;
        this.uploadKey = uploadKey;
    }
}
