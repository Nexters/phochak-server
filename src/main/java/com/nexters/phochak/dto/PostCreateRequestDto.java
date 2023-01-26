package com.nexters.phochak.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PostCreateRequestDto {

    private MultipartFile shorts;

    private List<String> hashtags;

    private String postCategory;

    public PostCreateRequestDto(MultipartFile shorts, List<String> hashtags, String postCategory) {
        this.shorts = shorts;
        this.hashtags = hashtags;
        this.postCategory = postCategory;
    }
}
