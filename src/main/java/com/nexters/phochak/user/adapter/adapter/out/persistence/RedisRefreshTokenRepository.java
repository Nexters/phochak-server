package com.nexters.phochak.user.adapter.adapter.out.persistence;

import com.nexters.phochak.common.config.property.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    @Override
    public void saveWithAccessToken(String refreshToken, String accessToken) {
        redisTemplate.opsForValue().set(refreshToken, accessToken, jwtProperties.getRefreshTokenExpireLength(), TimeUnit.MILLISECONDS);
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
