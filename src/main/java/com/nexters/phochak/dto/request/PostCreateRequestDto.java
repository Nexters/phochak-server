package com.nexters.phochak.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostCreateRequestDto {

    @NotNull
    private final MultipartFile shorts;

    @NotNull
    private final List<String> hashtags;

    @NotBlank
    private final String postCategory;

    public PostCreateRequestDto(MultipartFile shorts, List<String> hashtags, String postCategory) {
        this.shorts = shorts;
        this.hashtags = hashtags;
        this.postCategory = postCategory;
    }
}
