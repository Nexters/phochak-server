package com.nexters.phochak.dto;

import com.nexters.phochak.exception.ResCode;
import lombok.Getter;

@Getter
public class CommonResponseDto extends Response {

    public CommonResponseDto(ResCode resCode) {
        super(resCode, null);
    }

    public CommonResponseDto(ResCode resCode, String customResMessage) {
        super(resCode, customResMessage);
    }
}
