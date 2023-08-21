package com.nexters.phochak.common;

import com.nexters.phochak.common.exception.ResCode;
import lombok.Getter;

@Getter
public class StatusResponse {
    private String resCode;
    private String resMessage;

    public StatusResponse(ResCode resCode) {
        this(resCode.getCode(), resCode.getMessage());
    }

    public StatusResponse(String resCode, String resMessage) {
        this.resCode = resCode;
        this.resMessage = resMessage;
    }
}
