package com.nexters.phochak.dto;

import com.nexters.phochak.exception.ResCode;
import lombok.Getter;

@Getter
public class Response {

    private final String resCode;

    private final String resMessage;

    public Response(ResCode resCode, String customResMessage) {
        this.resCode = resCode.getCode();
        this.resMessage = customResMessage == null ? resCode.getMessage() : customResMessage;
    }

}
