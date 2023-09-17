package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.LikesEntity;
import com.nexters.phochak.post.adapter.out.persistence.LikesMapper;
import com.nexters.phochak.post.adapter.out.persistence.LikesRepository;
import com.nexters.phochak.post.domain.Likes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveLikesAdapter implements SaveLikesPort {

    private final LikesMapper likesMapper;
    private final LikesRepository likesRepository;
    @Override
    public void save(final Likes likes) {
        final LikesEntity likesEntity = likesMapper.toEntity(likes);
        try {
            likesRepository.save(likesEntity);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_PHOCHAKED);
        }
    }
}
