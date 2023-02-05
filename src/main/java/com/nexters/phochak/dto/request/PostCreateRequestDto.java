package com.nexters.phochak.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {

    @NotNull
    private String key;

    @NotNull
    private List<String> hashtags;

    @NotBlank
    private String postCategory;

    public PostCreateRequestDto(String key, List<String> hashtags, String postCategory) {
        this.key = key;
        this.hashtags = hashtags;
        this.postCategory = postCategory;
    }
}
