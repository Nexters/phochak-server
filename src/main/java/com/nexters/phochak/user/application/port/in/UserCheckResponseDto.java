package com.nexters.phochak.user.application.port.in;

import lombok.Getter;

@Getter
public class UserCheckResponseDto {
    private final Boolean isDuplicated;

    public UserCheckResponseDto(Boolean isDuplicated) {
        this.isDuplicated = isDuplicated;
    }
}
