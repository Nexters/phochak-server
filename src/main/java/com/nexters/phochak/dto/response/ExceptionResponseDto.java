package com.nexters.phochak.dto.response;

import com.nexters.phochak.exception.ResCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseDto extends Response {

    @Builder
    public ExceptionResponseDto(ResCode resCode, String customResMessage) {
        super(resCode, customResMessage);
    }
}
