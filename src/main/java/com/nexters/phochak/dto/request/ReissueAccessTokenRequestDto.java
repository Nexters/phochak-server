package com.nexters.phochak.dto.request;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;

import static com.nexters.phochak.dto.TokenDto.TOKEN_TYPE;

public class ReissueAccessTokenRequestDto {
    private String accessToken;
    private String refreshToken;

    public String getParsedAccessToken() {
        return parseToken(accessToken);
    }

    public String getParsedRefreshToken() {
        return parseToken(refreshToken);
    }

    private String parseToken(String original) {
        if (!accessToken.startsWith(TOKEN_TYPE + " ")) {
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }
        return accessToken.substring(TOKEN_TYPE.length()).trim();
    }
}
