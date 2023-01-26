package com.nexters.phochak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenDto {
    public static final String TOKEN_TYPE = "Bearer";
    private String tokenString;
    private String expiresIn;
}
