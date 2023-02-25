package com.nexters.phochak.dto.request;

import com.nexters.phochak.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NicknameModifyRequestDto {
    private static final int NICKNAME_MAX_SIZE = User.NICKNAME_MAX_SIZE;

    @NotNull
    @Size(min = 1, max = NICKNAME_MAX_SIZE)
    private String nickname;
}
