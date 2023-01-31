package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.HashtagService;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.PostSortCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    public static final long DEFAULT_PAGE_SIZE = 5;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShortsService shortsService;
    private final HashtagService hashtagService;

    @Override
    @Transactional
    public void create(Long userId, PostCreateRequestDto postCreateRequestDto) {
        User user = userRepository.getReferenceById(userId);
        Shorts shorts = shortsService.createShorts(postCreateRequestDto);
        Post post = Post.builder()
                        .user(user)
                        .postCategory(PostCategoryEnum.nameOf(postCreateRequestDto.getPostCategory()))
                        .shorts(shorts)
                        .build();
        hashtagService.createHashtagsByString(postCreateRequestDto.getHashtags(), post);
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPageResponseDto> getNextCursorPage(Long lastId, Long pageSize, String postSortCriteria, Long lastCriteriaValue) {
        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        validatePostSortCriteriaAndCriteriaValue(postSortCriteria, lastCriteriaValue);

        postRepository.findNextPageByCursor(lastId, pageSize, postSortCriteria, lastCriteriaValue);

        return null;
    }

    private static void validatePostSortCriteriaAndCriteriaValue(String postSortCriteria, Long lastCriteriaValue) {
        PostSortCriteria postSortCriteriaEnum = PostSortCriteria.nameOf(postSortCriteria);

        if (Objects.isNull(lastCriteriaValue) && postSortCriteriaEnum != PostSortCriteria.LATEST) {
            throw new PhochakException(ResCode.NOT_FOUND_LAST_CRITERIA_VALUE);
        }
    }
}
