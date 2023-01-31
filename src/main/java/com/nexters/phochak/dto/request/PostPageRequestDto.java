package com.nexters.phochak.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostPageRequestDto {
    @NotNull
    private Long lastId;
    private Long pageSize;
    @NotNull
    private String postSortCriteria;
    private Long lastCriteriaValue;

    public PostPageRequestDto(Long lastId, Long pageSize, String postSortCriteria, Long lastCriteriaValue) {
        this.lastId = lastId;
        this.pageSize = pageSize;
        this.postSortCriteria = postSortCriteria;
        this.lastCriteriaValue = lastCriteriaValue;
    }
}
