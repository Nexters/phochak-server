package com.nexters.phochak.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {

    @NotNull
    @Size(max = 30)
    private List<String> hashtags;

    @NotBlank
    private String category;

    @Builder
    public PostUpdateRequestDto(List<String> hashtags, String category) {
        this.hashtags = hashtags;
        this.category = category;
    }
}
