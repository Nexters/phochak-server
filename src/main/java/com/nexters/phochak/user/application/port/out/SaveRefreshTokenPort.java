package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;

public interface SaveRefreshTokenPort {
    void save(JwtTokenUseCase.TokenVo accessToken, JwtTokenUseCase.TokenVo refreshToken);
}
