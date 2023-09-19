package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.DeleteAllPostPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllPostAdapter implements DeleteAllPostPort {

    private final PostRepository postRepository;

    @Override
    public void deleteAllPostByUserId(final Long userId) {
        postRepository.deleteAllByUserId(userId);
    }
}
