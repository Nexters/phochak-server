package com.nexters.phochak.dto.response;

import com.nexters.phochak.exception.ResCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonResponse<T> {
    private StatusResponse status = new StatusResponse(ResCode.OK);
    private final T data;

    public CommonResponse() {
        this.data = null;
    }

    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse(ResCode resCode) {
        this();
        this.status = new StatusResponse(resCode);
    }

    public CommonResponse(String resCode, String resMessage) {
        this();
        this.status = new StatusResponse(resCode, resMessage);
    }
}
