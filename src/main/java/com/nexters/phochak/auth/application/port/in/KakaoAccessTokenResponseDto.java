package com.nexters.phochak.auth.application.port.in;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonNaming(SnakeCaseStrategy.class)
@NoArgsConstructor
@ToString
@Getter
public class KakaoAccessTokenResponseDto {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;
    private String refreshTokenExpiresIn;

    public KakaoAccessTokenResponseDto(String accessToken, String tokenType, String refreshToken, String expiresIn, String refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
