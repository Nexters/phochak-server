package com.nexters.phochak.auth.adapter.out.persistence;

public interface RefreshTokenRepository {

    void saveWithAccessToken(String refreshToken, String accessToken);

    String findAccessToken(String refreshToken);

    Boolean expire(String refreshToken);
}
