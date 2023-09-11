package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.SavePostPort;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SavePostAdapter implements SavePostPort {
    private final PostMapper postMapper;
    private final PostRepository postRepository;

    @Override
    public void save(final Post post) {
        final PostEntity postEntity = postMapper.toEntity(post);
        postRepository.save(postEntity);
        post.assignId(postEntity.getId());
    }
}
