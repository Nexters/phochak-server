package com.nexters.phochak.user.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NicknameModifyRequestDto(
        @NotNull
        @Size(min = 1, max = 10)
        String nickname) {
    public NicknameModifyRequestDto(final String nickname) {
        this.nickname = nickname;
    }
}
