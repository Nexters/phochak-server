package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadPostAdapter implements LoadPostPort {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Post load(final Long postId) {
        final PostEntity postEntity = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        return postMapper.toDomain(postEntity);
    }
}
