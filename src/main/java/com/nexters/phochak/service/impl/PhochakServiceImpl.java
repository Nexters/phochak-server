package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Likes;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.LikesRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.PhochakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhochakServiceImpl implements PhochakService {

    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addPhochak(Long userId, Long postId) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        if(likesRepository.existsByUserAndPost(user, post)) {
            throw new PhochakException(ResCode.ALREADY_PHOCHAKED);
        }

        Likes likes = Likes.builder()
                .user(user)
                .post(post)
                .build();
        likesRepository.save(likes);
    }

    @Override
    @Transactional
    public void cancelPhochak(Long userId, Long postId) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));

        Likes likes = likesRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new PhochakException(ResCode.NOT_PHOCHAKED));

        likesRepository.delete(likes);
    }

}
