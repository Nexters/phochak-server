package com.nexters.phochak.service;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShortsServiceImpl shortsService;

    @Override
    @Transactional
    public void create(LoginUser loginUser, PostCreateRequestDto postCreateRequestDto) {
        User user = userRepository.findById(loginUser.getUserId).orElseThrow(new PhochakException(ResCode.USER_NOT_VALID));
        Shorts shorts = shortsService.createShorts(postCreateRequestDto.getMultipartFile());
        Post post = Post.builder()
                        .user(user)
                        .postCategory(postCreateRequestDto.getPostCategory())
                        .shorts(shorts)
                        .build();
        //ToDo 해시태그 저장
        postRepository.save(post);
    }
}
