package com.nexters.phochak.common;

import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;

import java.util.concurrent.TimeUnit;

public class CreateAccessTokenQuery {

    private long userId = 1L;
    private long expireLength = TimeUnit.MINUTES.toMillis(60L);

    public CreateAccessTokenQuery userId(long userId) {
        this.userId = userId;
        return this;
    }

    public CreateAccessTokenQuery expireLengthByMinutes(long expireLength) {
        this.expireLength = expireLength;
        return this;
    }

    public String getAccessToken() {
        final JwtTokenUseCase.TokenVo tokenVo = RestDocsApiTest.Util.jwtTokenUseCase.generateToken(userId, expireLength);
        return JwtTokenUseCase.TokenVo.TOKEN_TYPE + " " + tokenVo.getTokenString();
    }
}
