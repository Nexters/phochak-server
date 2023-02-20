package com.nexters.phochak.repository;

public interface RefreshTokenRepository {

    void saveWithAccessToken(String refreshToken, String accessToken);

    String findAccessToken(String refreshToken);

    Boolean expire(String refreshToken);
}
