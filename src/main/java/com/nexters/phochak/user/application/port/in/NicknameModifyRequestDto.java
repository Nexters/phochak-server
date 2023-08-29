package com.nexters.phochak.user.application.port.in;

import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameModifyRequestDto {
    private static final int NICKNAME_MAX_SIZE = UserEntity.NICKNAME_MAX_SIZE;

    @NotNull
    @Size(min = 1, max = NICKNAME_MAX_SIZE)
    private String nickname;
}
