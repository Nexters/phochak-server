package com.nexters.phochak.user.application.port.out;

public interface FindAccessTokenPort {
    String find(String currentRefreshToken);
}
