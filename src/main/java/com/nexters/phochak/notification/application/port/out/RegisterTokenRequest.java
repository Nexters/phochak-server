package com.nexters.phochak.notification.application.port.out;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterTokenRequest(
        @NotNull(message = "userId는 필수입니다.")
        Long userId,
        @NotBlank(message = "token은 필수입니다.")
        String token) {
}
