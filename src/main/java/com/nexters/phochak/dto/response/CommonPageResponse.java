package com.nexters.phochak.dto.response;

import lombok.Getter;

import java.util.List;

import static com.nexters.phochak.exception.ResCode.OK;

@Getter
public class CommonPageResponse<T> {

    private final String resCode = OK.getCode();

    private final String resMessage = OK.getMessage();

    private Boolean isLastPage;
    private final List<T> data;

    public CommonPageResponse() {
        this.data = null;
    }

    public CommonPageResponse(List<T> data) {
        this(data, false);
    }

    public CommonPageResponse(List<T> data, Boolean isLastPage) {
        this.data = data;
        this.isLastPage = isLastPage;
    }
}
