package com.nexters.phochak.user.application.application.port.in;

import com.nexters.phochak.notification.domain.OperatingSystem;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String token,
        String fcmDeviceToken,
        OperatingSystem operatingSystem){
}
