package com.nexters.phochak.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostCreateRequestDto {

    @NotNull
    private final String key;

    @NotNull
    private final List<String> hashtags;

    @NotBlank
    private final String postCategory;

    public PostCreateRequestDto(String key, List<String> hashtags, String postCategory) {
        this.key = key;
        this.hashtags = hashtags;
        this.postCategory = postCategory;
    }
}
