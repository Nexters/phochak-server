package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.DeletePostPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletePostAdapter implements DeletePostPort {

    private final PostRepository postRepository;

    @Override
    public void delete(Post post) {
        postRepository.deleteById(post.getId());
    }

    @Override
    public void deleteAllByUser(final User user) {
        postRepository.deleteAllByUserId(user.getId());
    }
}
