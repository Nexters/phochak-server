package com.nexters.phochak.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto {

    private final String resCode;

    private final String resMessage;

    @Builder
    public ExceptionResponseDto(String resCode, String resMessage, String resDetail) {
        this.resCode = resCode;
        this.resMessage = resMessage;
    }
}
