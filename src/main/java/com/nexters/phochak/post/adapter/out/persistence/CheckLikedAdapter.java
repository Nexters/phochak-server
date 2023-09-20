package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.LikesFetchDto;
import com.nexters.phochak.post.application.port.out.CheckLikedPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckLikedAdapter implements CheckLikedPort {

    private final LikesRepository likesRepository;

    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPostList(final List<Long> postIds, final Long userId) {
        return likesRepository.checkIsLikedPost(postIds, userId);
    }
}
