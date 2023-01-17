package com.nexters.phochak.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto {

    private String resCode;

    private String resMessage;

    private String detail = " ";

    @Builder
    public ExceptionResponseDto(String resCode, String resMessage, String detail) {
        this.resCode = resCode;
        this.resMessage = resMessage;
        this.detail = detail;
    }
}
