package com.nexters.phochak.repository;

import com.nexters.phochak.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository {

    void saveWithAccessToken(String refreshToken, String accessToken);

    String findAccessToken(String refreshToken);

    String expire(String refreshToken);
}
