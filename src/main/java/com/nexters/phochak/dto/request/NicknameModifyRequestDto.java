package com.nexters.phochak.dto.request;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class NicknameModifyRequestDto {
    private static final int NICKNAME_MAX_SIZE = 10;

    @Size(min = 1, max = NICKNAME_MAX_SIZE)
    private String nickname;
}
