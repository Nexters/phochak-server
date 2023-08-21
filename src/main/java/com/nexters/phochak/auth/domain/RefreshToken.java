package com.nexters.phochak.auth.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
