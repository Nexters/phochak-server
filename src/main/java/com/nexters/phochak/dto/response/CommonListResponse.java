package com.nexters.phochak.dto.response;

import lombok.Getter;

import java.util.List;

import static com.nexters.phochak.exception.ResCode.OK;

@Getter
public class CommonListResponse<T> {

    private final String resCode = OK.getCode();

    private final String resMessage = OK.getMessage();

    private final List<T> data;

    public CommonListResponse() {
        this.data = null;
    }

    public CommonListResponse(List<T> data) {
        this.data = data;
    }
}
