package com.nexters.phochak.dto.request;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.specification.PostSortOption;
import lombok.Builder;
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
    private PostSortOption sortOption;
    private Integer sortValue;

    @Builder
    public CustomCursor(Long lastId, Integer pageSize, PostSortOption sortOption, Integer sortValue) {
        this.lastId = lastId;
        this.pageSize = Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
        this.sortOption = sortOption;
        this.sortValue = sortValue;

        // 첫 요청 시 lastId, sortValue는 null로, sortOption만 받음
        if (Objects.isNull(lastId) && Objects.isNull(sortValue)) {
            if (Objects.isNull(sortOption)) {
                throw new PhochakException(ResCode.NOT_FOUND_SORT_OPTION);
            }
            this.lastId = Long.MAX_VALUE;
            this.sortValue = Integer.MAX_VALUE;
        } else {
            if (Objects.isNull(sortValue) && sortOption != PostSortOption.LATEST) {
                throw new PhochakException(ResCode.NOT_FOUND_SORT_VALUE);
            }
        }
    }

    public String createCursorString() {
        if (Objects.isNull(sortValue)) {
            return null;
        }

        return String.format(INTEGER_STRING_FORMATTER, sortValue) + String.format(LONG_STRING_FORMATTER, lastId);
    }
}
