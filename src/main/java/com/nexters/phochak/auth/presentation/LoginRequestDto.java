package com.nexters.phochak.auth.presentation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank
    private String token;
}
