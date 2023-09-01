package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.common.StatusResponse;
import com.nexters.phochak.common.exception.ResCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommonPageResponseDto<T> {
    private StatusResponse status = new StatusResponse(ResCode.OK);
    private final List<T> data;
    private Boolean isLastPage;

    public CommonPageResponseDto() {
        this.data = null;
    }

    public CommonPageResponseDto(List<T> data) {
        this(data, false);
    }

    public CommonPageResponseDto(List<T> data, Boolean isLastPage) {
        this.data = data;
        this.isLastPage = isLastPage;
    }
}
