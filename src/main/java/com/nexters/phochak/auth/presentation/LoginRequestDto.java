package com.nexters.phochak.auth.presentation;

import jakarta.validation.constraints.NotBlank;

import javax.annotation.Nullable;

public record LoginRequestDto(
        @NotBlank String token,
        @Nullable String fcmDeviceToken) {
}
