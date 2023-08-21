package com.nexters.phochak.auth.presentation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginV2RequestDto {

    @NotBlank
    private String token;

    @NotBlank
    private String fcmDeviceToken;
}
