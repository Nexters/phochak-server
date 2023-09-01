package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import com.nexters.phochak.post.domain.PostCategoryEnum;
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
    private final Long targetUserId;
    private final String searchHashtag;
    private final PostCategoryEnum category;

    public static PostFetchCommand of(CustomCursorDto customCursorDto, long userId) {
        return PostFetchCommand.builder()
                .userId(userId)
                .lastId(customCursorDto.getLastId())
                .pageSize(customCursorDto.getPageSize())
                .sortOption(customCursorDto.getSortOption())
                .sortValue(customCursorDto.getSortValue())
                .filter(customCursorDto.getFilter())
                .targetUserId(customCursorDto.getTargetUserId())
                .searchHashtag(null)
                .category(null)
                .build();
    }

    public static PostFetchCommand of(CustomCursorDto customCursorDto, long userId, String hashtag) {
        return PostFetchCommand.builder()
                .userId(userId)
                .lastId(customCursorDto.getLastId())
                .pageSize(customCursorDto.getPageSize())
                .sortOption(customCursorDto.getSortOption())
                .sortValue(customCursorDto.getSortValue())
                .filter(PostFilter.SEARCH)
                .targetUserId(customCursorDto.getTargetUserId())
                .searchHashtag(hashtag)
                .category(customCursorDto.getCategory())
                .build();
    }

    public boolean hasUploadedFilter() {
        return Objects.equals(filter, PostFilter.UPLOADED);
    }

}
