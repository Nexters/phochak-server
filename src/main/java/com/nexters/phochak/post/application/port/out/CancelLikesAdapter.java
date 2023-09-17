package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.adapter.out.persistence.LikesEntity;
import com.nexters.phochak.post.adapter.out.persistence.LikesMapper;
import com.nexters.phochak.post.adapter.out.persistence.LikesRepository;
import com.nexters.phochak.post.domain.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelLikesAdapter implements CancelLikesPort {

    private final LikesMapper likesMapper;
    private final LikesRepository likesRepository;

    @Override
    public void cancel(final Likes likes) {
        final LikesEntity likesEntity = likesMapper.toEntity(likes);
        likesRepository.delete(likesEntity);
    }
}
