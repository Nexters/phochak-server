package com.nexters.phochak.user.adapter.out.persistence;

public interface ExpireRefreshTokenPort {
    void expire(String currentRefreshToken);
}
