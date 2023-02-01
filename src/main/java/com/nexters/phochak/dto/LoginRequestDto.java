package com.nexters.phochak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank
    private String token;
}
