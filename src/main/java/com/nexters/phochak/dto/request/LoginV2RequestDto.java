package com.nexters.phochak.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginV2RequestDto {
    
    @NotBlank
    private String token;

    @NotBlank
    private String fcmDeviceToken;
}
