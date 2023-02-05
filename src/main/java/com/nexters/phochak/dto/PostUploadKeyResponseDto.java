package com.nexters.phochak.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUploadKeyResponseDto {

    private final String uploadUrl;

    private final String key;

    @Builder
    public PostUploadKeyResponseDto(String uploadUrl, String key) {
        this.uploadUrl = uploadUrl;
        this.key = key;
    }
}
