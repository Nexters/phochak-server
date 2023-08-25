package com.nexters.phochak.user;

import lombok.Getter;

@Getter
public class UserCheckResponseDto {
    private final Boolean isDuplicated;

    private UserCheckResponseDto(Boolean isDuplicated) {
        this.isDuplicated = isDuplicated;
    }

    public static UserCheckResponseDto of(Boolean isDuplicated) {
        return new UserCheckResponseDto(isDuplicated);
    }
}
