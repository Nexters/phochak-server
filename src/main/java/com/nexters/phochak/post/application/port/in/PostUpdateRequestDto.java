package com.nexters.phochak.post.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostUpdateRequestDto(
        @NotNull
        @Size(max = 30)
        List<String> hashtags,
        @NotBlank
        String category
) {
}