package com.nexters.phochak.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;
    private Long userId;
    private String refreshTokenString;

    @Builder
    public RefreshToken(Long userId, String refreshTokenString) {
        this.userId = userId;
        this.refreshTokenString = refreshTokenString;
    }
}
