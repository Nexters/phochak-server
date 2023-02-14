package com.nexters.phochak.dto.request;

import lombok.Getter;

@Getter
public class ReissueAccessTokenRequestDto {
    private String accessToken;
    private String refreshToken;

}
