package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.BlindPostPort;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BlindPostAdapter implements BlindPostPort {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public void blind(final Post post) {
        post.blind();
        final PostEntity postEntity = postMapper.toEntity(post);
        postRepository.save(postEntity);
    }
}
