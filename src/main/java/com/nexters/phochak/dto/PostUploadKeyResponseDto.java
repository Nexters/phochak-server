package com.nexters.phochak.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUploadKeyResponseDto {

    private final String uploadUrl;

    private final String objectName;

    @Builder
    public PostUploadKeyResponseDto(String uploadUrl, String objectName) {
        this.uploadUrl = uploadUrl;
        this.objectName = objectName;
    }
}
