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
    private final long userId;
    private final Long lastId;
    private final Integer pageSize;
    private final PostSortOption sortOption;
    private final Integer sortValue;
    private final PostFilter filter;
    private final String searchHashtag;

    public static PostFetchCommand of(CustomCursor customCursor, long userId) {
        return PostFetchCommand.builder()
                .userId(userId)
                .lastId(customCursor.getLastId())
                .pageSize(customCursor.getPageSize())
                .sortOption(customCursor.getSortOption())
                .sortValue(customCursor.getSortValue())
                .filter(customCursor.getFilter())
                .searchHashtag(null)
                .build();
    }

    public static PostFetchCommand of(CustomCursor customCursor, long userId, String hashtag) {
        return PostFetchCommand.builder()
                .userId(userId)
                .lastId(customCursor.getLastId())
                .pageSize(customCursor.getPageSize())
                .sortOption(customCursor.getSortOption())
                .sortValue(customCursor.getSortValue())
                .filter(PostFilter.SEARCH)
                .searchHashtag(hashtag)
                .build();
    }

    public boolean hasUploadedFilter() {
        return Objects.equals(filter, PostFilter.UPLOADED);
    }

}
