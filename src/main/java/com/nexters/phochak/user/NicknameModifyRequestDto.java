package com.nexters.phochak.user;

import com.nexters.phochak.user.domain.UserEntity;
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
