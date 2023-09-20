package com.nexters.phochak.user.adapter.port.out.persistence;

import com.nexters.phochak.user.adapter.out.persistence.RefreshTokenRepository;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.port.out.SaveRefreshTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRefreshTokenAdapter implements SaveRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void save(final JwtTokenUseCase.TokenVo accessToken, final JwtTokenUseCase.TokenVo refreshToken) {
        refreshTokenRepository.saveWithAccessToken(refreshToken.getTokenString(), accessToken.getTokenString());
    }
}
