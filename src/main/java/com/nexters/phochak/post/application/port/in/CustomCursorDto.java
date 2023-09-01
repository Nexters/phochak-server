package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.PostFilter;
import com.nexters.phochak.post.adapter.out.persistence.PostSortOption;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
public class CustomCursorDto {
    private static final int DEFAULT_PAGE_SIZE = 5;

    private Long lastId;
    private Integer pageSize;
    private PostSortOption sortOption;
    private Integer sortValue;
    private PostFilter filter;
    private Long targetUserId;
    private PostCategoryEnum category;

    @Builder
    public CustomCursorDto(Long lastId, Integer pageSize, PostSortOption sortOption, Integer sortValue, PostFilter filter, Long targetUserId, PostCategoryEnum category) {
        this.lastId = lastId;
        this.pageSize = Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
        this.sortOption = Objects.isNull(sortOption) ? PostSortOption.LATEST : sortOption;
        this.sortValue = sortValue;
        this.filter = Objects.isNull(filter) ? PostFilter.NONE : filter;
        this.targetUserId = targetUserId;
        this.category = category;

        // 첫 요청 시 lastId, sortValue는 null로, sortOption만 받음
        if (Objects.isNull(lastId) && Objects.isNull(this.sortValue)) {
            this.lastId = Long.MAX_VALUE;
            this.sortValue = Integer.MAX_VALUE;
        } else {
            if (Objects.isNull(this.sortValue) && (this.sortOption != PostSortOption.LATEST)) {
                throw new PhochakException(ResCode.NOT_FOUND_SORT_VALUE);
            }
        }
    }
}
