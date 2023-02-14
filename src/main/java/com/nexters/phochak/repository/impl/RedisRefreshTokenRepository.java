package com.nexters.phochak.repository.impl;

import com.nexters.phochak.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    @Value("${security.jwt.token.refresh-token-expire-length}")
    private long refreshTokenExpireLength;

    @Override
    public void saveWithAccessToken(String refreshToken, String accessToken) {
        redisTemplate.opsForValue().set(refreshToken, accessToken, refreshTokenExpireLength, TimeUnit.MILLISECONDS);
    }

    @Override
    public String findAccessToken(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    @Override
    public Boolean expire(String refreshToken) {
        return redisTemplate.delete(refreshToken);
    }
}
