package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.response.PostPageResponseDto;

import java.util.List;

public interface PostService {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);

    /**
     * 다음 커서로부터 Page size만큼의 page를 가져온다.
     * @param lastId
     * @param pageSize
     * @param postSortCriteria
     * @param lastCriteriaValue
     * @return
     */
    List<PostPageResponseDto> getNextCursorPage(Long lastId, Long pageSize, String postSortCriteria, Long lastCriteriaValue);
}
