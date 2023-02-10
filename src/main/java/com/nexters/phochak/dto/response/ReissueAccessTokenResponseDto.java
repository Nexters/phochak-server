package com.nexters.phochak.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueAccessTokenResponseDto {
    private String accessToken;
    private String expiresIn;
}
