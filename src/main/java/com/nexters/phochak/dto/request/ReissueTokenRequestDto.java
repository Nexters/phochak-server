package com.nexters.phochak.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueTokenRequestDto {
    private String accessToken;
    private String refreshToken;

}
