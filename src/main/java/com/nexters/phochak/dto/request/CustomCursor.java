package com.nexters.phochak.dto.request;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.specification.PostSortCriteria;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@ToString
@Getter
public class CustomCursor {
    private static final int INITIAL_REQUEST_NUMBER = -1;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String INTEGER_STRING_FORMATTER = "%010d";
    private static final String LONG_STRING_FORMATTER = "%019d";
    private static final int CURSOR_SIZE = 16;
    private static final String MAX_DIGIT = "9";

    private Long lastId;
    private Integer pageSize;
    @NotNull
    private PostSortCriteria postSortCriteria;
    private Integer lastCriteriaValue;
    private Boolean isInitialRequest;

    public CustomCursor(Long lastId, Integer pageSize, PostSortCriteria postSortCriteria, Integer lastCriteriaValue, Boolean isInitialRequest) {
        this.lastId = lastId;
        this.pageSize = Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
        this.postSortCriteria = postSortCriteria;
        this.lastCriteriaValue = lastCriteriaValue;
        this.isInitialRequest = Objects.isNull(isInitialRequest) ? Boolean.FALSE : isInitialRequest;

        if (Boolean.TRUE.equals(isInitialRequest)) {
            this.lastId = Long.MAX_VALUE;
            this.lastCriteriaValue = Integer.MAX_VALUE;
        } else {
            if (Objects.isNull(lastCriteriaValue) && postSortCriteria != PostSortCriteria.LATEST) {
                throw new PhochakException(ResCode.NOT_FOUND_LAST_CRITERIA_VALUE);
            }
        }
    }

    public String createCursorString() {
        if (Objects.isNull(lastCriteriaValue)) {
            return null;
        }

        return String.format(INTEGER_STRING_FORMATTER, lastCriteriaValue) + String.format(LONG_STRING_FORMATTER, lastId);
    }
}
