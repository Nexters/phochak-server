package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.HashtagService;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.specification.PostCategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

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

}
