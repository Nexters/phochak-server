package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.out.FindAccessTokenPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAccessTokenAdapter implements FindAccessTokenPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String find(final String currentRefreshToken) {
        return refreshTokenRepository.findAccessToken(currentRefreshToken);
    }
}
