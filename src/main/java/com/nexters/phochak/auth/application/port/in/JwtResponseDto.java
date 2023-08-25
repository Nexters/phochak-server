package com.nexters.phochak.auth.application.port.in;

import lombok.Builder;

@Builder
public record JwtResponseDto(String accessToken, String expiresIn, String refreshToken, String refreshTokenExpiresIn) {
}