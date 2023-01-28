package com.nexters.phochak.dto.response;

import lombok.Getter;

import static com.nexters.phochak.exception.ResCode.OK;

@Getter
public class CommonResponse<T> {

    private final String resCode = OK.getCode();;

    private final String resMessage = OK.getMessage();;

    private final T data;

    public CommonResponse() {
        this.data = null;
    }

    public CommonResponse(T data) {
        this.data = data;
    }
}
