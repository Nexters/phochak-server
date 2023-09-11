package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostUseCase {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);

    PostUploadKeyResponseDto generateUploadKey(String fileExtension);

    /**
     * 커서로부터 Page size만큼의 page를 가져온다.
     * @param customCursorDto
     * @return
     */
    List<PostPageResponseDto> getNextCursorPage(CustomCursorDto customCursorDto);

    void update(Long userId, Long postId, PostUpdateRequestDto postUpdateRequestDto);

    void delete(Long userId, Long postId);

    @Transactional(readOnly = true)
    List<PostPageResponseDto> getNextCursorPage(CustomCursorDto customCursorDto, String hashtag);

    /**
     * 특정 게시글의 조회수를 올린다.
     * @param postId
     */
    int updateView(Long postId);

    void deleteAllPostByUser(UserEntity userEntity);

    List<String> getHashtagAutocomplete(String hashtag, int size);
}
