package com.nexters.phochak.auth.presentation;

import lombok.Builder;

@Builder
public record JwtResponseDto(String accessToken, String expiresIn, String refreshToken, String refreshTokenExpiresIn) {
}