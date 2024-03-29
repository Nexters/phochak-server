package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.shorts.application.port.in.PostUploadKeyResponseDto;

import java.util.List;

public interface PostUseCase {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);

    PostUploadKeyResponseDto generateUploadKey(String fileExtension);


    void update(Long userId, Long postId, PostUpdateRequestDto postUpdateRequestDto);

    void delete(Long userId, Long postId);

    List<PostPageResponseDto> getPostPage(Long userId, CustomCursorDto customCursorDto);

    /**
     * 특정 게시글의 조회수를 올린다.
     *
     * @param postId
     */
    void updateView(Long postId);

    void deleteAllPost(Long userId);

    List<String> getHashtagAutocomplete(String hashtag, int size);
}
