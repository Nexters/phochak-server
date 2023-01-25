package com.nexters.phochak.service;

import com.nexters.phochak.domain.Hashtag;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShortsServiceImpl shortsService;
    private final HashtagServiceImpl hashtagSerivce;

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
        hashtagSerivce.createHashtagsByString(postCreateRequestDto.getHashtags(), post);
        postRepository.save(post);
    }
}
