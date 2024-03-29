package com.nexters.phochak.user.application.port.in;

import lombok.Builder;

@Builder
public record JwtResponseDto(String accessToken, String expiresIn, String refreshToken, String refreshTokenExpiresIn) {
}