package com.nexters.phochak.user.adapter.out.web;

import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllPostNetworkMonolithicClient implements DeleteAllPostNetworkClient {

    private final PostRepository postRepository;

    @Override
    public void deleteAllPostByUser(final Long userId) {
        postRepository.deleteAllByUserId(userId);
    }
}
