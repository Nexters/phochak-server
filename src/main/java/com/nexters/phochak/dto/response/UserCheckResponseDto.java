package com.nexters.phochak.dto.response;

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
