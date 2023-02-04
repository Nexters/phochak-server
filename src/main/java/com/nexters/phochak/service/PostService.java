package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.response.PostPageResponseDto;

import java.util.List;

public interface PostService {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);

    PostUploadKeyResponseDto generateUploadKey(String fileExtension);
    
    PostUploadKeyResponseDto generateUploadKey(Long userId, PostUploadKeyRequestDto postUploadKeyRequestDto);

    /**
     * 커서로부터 Page size만큼의 page를 가져온다.
     * @param customCursor
     * @return
     */
    List<PostPageResponseDto> getNextCursorPage(CustomCursor customCursor);
}
