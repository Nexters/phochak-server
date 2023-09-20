package com.nexters.phochak.user.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpireRefreshTokenAdapter implements ExpireRefreshTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void expire(final String currentRefreshToken) {
        refreshTokenRepository.expire(currentRefreshToken);
    }
}
