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
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final PostSortOption defaultPostSortOption = PostSortOption.LATEST;

    private Long lastId;
    private Integer pageSize;
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
            this.lastId = Long.MAX_VALUE;
            this.sortValue = Integer.MAX_VALUE;
        } else {
            if (Objects.isNull(sortValue) && (sortOption != PostSortOption.LATEST && sortOption != null)) {
                throw new PhochakException(ResCode.NOT_FOUND_SORT_VALUE);
            }
        }
    }
}
