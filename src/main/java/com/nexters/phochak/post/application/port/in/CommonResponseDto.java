package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.common.StatusResponse;
import com.nexters.phochak.common.exception.ResCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommonResponseDto<T> {
    private StatusResponse status = new StatusResponse(ResCode.OK);
    private final T data;

    public CommonResponseDto() {
        this.data = null;
    }

    public CommonResponseDto(T data) {
        this.data = data;
    }

    public CommonResponseDto(ResCode resCode) {
        this();
        this.status = new StatusResponse(resCode);
    }

    public CommonResponseDto(String resCode, String resMessage) {
        this();
        this.status = new StatusResponse(resCode, resMessage);
    }
}
