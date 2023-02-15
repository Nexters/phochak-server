package com.nexters.phochak.dto;

import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostFilter;
import com.nexters.phochak.specification.PostSortOption;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Builder
@RequiredArgsConstructor
@Getter
public class PostFetchCommand {
    private static final String INTEGER_STRING_FORMATTER = "%010d";
    private static final String LONG_STRING_FORMATTER = "%019d";

    private final long userId;
    private final Long lastId;
    private final Integer pageSize;
    private final PostSortOption sortOption;
    private final Integer sortValue;
    private final PostFilter filter;

    public static PostFetchCommand of(CustomCursor customCursor, PostFilter filter, long userId) {
        return PostFetchCommand.builder()
                .userId(userId)
                .lastId(customCursor.getLastId())
                .pageSize(customCursor.getPageSize())
                .sortOption(customCursor.getSortOption())
                .sortValue(customCursor.getSortValue())
                .filter(filter)
                .build();
    }

    public boolean hasUploadedFilter() {
        return filter.isUploadedFilter();
    }

    public String createCursorString() {
        if (Objects.isNull(sortValue)) {
            return null;
        }

        return String.format(INTEGER_STRING_FORMATTER, sortValue) + String.format(LONG_STRING_FORMATTER, lastId);
    }
}
