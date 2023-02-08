package com.nexters.phochak.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private String uploadKey;

    @NotNull
    @Size(max = 30)
    private List<String> hashtags;

    @NotBlank
    private String postCategory;

    @Builder
    public PostCreateRequestDto(String uploadKey, List<String> hashtags, String postCategory) {
        this.uploadKey = uploadKey;
        this.hashtags = hashtags;
        this.postCategory = postCategory;
    }
}
