package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Phochak;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PhochakRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.PhochakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhochakServiceImpl implements PhochakService {

    private final PhochakRepository phochakRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void addPhochak(Long userId, Long postId) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        if(phochakRepository.existsByUserAndPost(user, post)) {
            throw new PhochakException(ResCode.ALREADY_PHOCHAKED);
        }

        Phochak phochak = Phochak.builder()
                .user(user)
                .post(post)
                .build();
        phochakRepository.save(phochak);
    }
}
