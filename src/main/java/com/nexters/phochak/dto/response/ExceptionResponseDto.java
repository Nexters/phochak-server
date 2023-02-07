package com.nexters.phochak.dto.response;

import com.nexters.phochak.exception.ResCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto {
    private StatusResponse status;

    @Builder
    public ExceptionResponseDto(ResCode resCode, String customResMessage) {
        status = new StatusResponse(resCode.getCode(), customResMessage == null ? resCode.getMessage() : customResMessage);
    }
}
