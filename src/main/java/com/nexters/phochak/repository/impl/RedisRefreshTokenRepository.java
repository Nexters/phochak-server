package com.nexters.phochak.repository.impl;

import com.nexters.phochak.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    @Override
    public void saveWithAccessToken(String refreshToken, String accessToken) {

    }

    @Override
    public String findAccessToken(String refreshToken) {
        return null;
    }

    @Override
    public String expire(String refreshToken) {
        return null;
    }
}
