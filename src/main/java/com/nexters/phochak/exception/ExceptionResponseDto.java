package com.nexters.phochak.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto {

    private String resCode;

    private String resMessage;

    private String resDetail;

    @Builder
    public ExceptionResponseDto(String resCode, String resMessage, String resDetail) {
        this.resCode = resCode;
        this.resMessage = resMessage;
        this.resDetail = resDetail;
    }
}
