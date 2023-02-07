package com.nexters.phochak.dto.response;

import com.nexters.phochak.exception.ResCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommonPageResponse<T> {
    private StatusResponse status = new StatusResponse(ResCode.OK);
    private final List<T> data;
    private Boolean isLastPage;

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
