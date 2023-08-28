package com.nexters.phochak.auth.application.port.in;

import com.nexters.phochak.notification.domain.OperatingSystem;
import jakarta.validation.constraints.NotBlank;

import javax.annotation.Nullable;

public record LoginRequestDto(
        @NotBlank String token,
        @Nullable String fcmDeviceToken,
        @Nullable OperatingSystem operatingSystem){
}
