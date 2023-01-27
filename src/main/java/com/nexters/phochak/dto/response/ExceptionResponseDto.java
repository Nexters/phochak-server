package com.nexters.phochak.dto.response;

import com.nexters.phochak.exception.ResCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto {

    private final String resCode;

    private final String resMessage;

    @Builder
    public ExceptionResponseDto(ResCode resCode, String customResMessage) {
        this.resCode = resCode.getCode();
        this.resMessage = customResMessage == null ? resCode.getMessage() : customResMessage;
    }
}
