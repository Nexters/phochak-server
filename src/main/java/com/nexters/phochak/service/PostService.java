package com.nexters.phochak.service;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.PostFilter;
import com.nexters.phochak.dto.request.PostUpdateRequestDto;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostService {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);

    PostUploadKeyResponseDto generateUploadKey(String fileExtension);

    /**
     * 커서로부터 Page size만큼의 page를 가져온다.
     * @param customCursor
     * @return
     */
    List<PostPageResponseDto> getNextCursorPage(CustomCursor customCursor, PostFilter filter);

    void update(Long userId, Long postId, PostUpdateRequestDto postUpdateRequestDto);

    void delete(Long userId, Long postId);

    @Transactional(readOnly = true)
    List<PostPageResponseDto> getNextCursorPage(CustomCursor customCursor, String hashtag);

    /**
     * 특정 게시글의 조회수를 올린다.
     * @param postId
     */
    int updateView(Long postId);

    void deleteAllPostByUser(User user);

    List<String> getHashtagAutocomplete(String hashtag, int size);
}
