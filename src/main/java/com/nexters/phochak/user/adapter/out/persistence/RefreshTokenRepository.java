package com.nexters.phochak.user.adapter.out.persistence;

public interface RefreshTokenRepository {

    void saveWithAccessToken(String refreshToken, String accessToken);

    String findAccessToken(String refreshToken);

    Boolean expire(String refreshToken);
}
