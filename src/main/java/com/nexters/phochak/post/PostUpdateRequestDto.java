package com.nexters.phochak.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
