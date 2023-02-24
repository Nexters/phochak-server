package com.nexters.phochak.service.impl;

import com.nexters.phochak.dto.LikesFetchDto;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;
import com.nexters.phochak.repository.LikesRepository;
import com.nexters.phochak.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikesService {
    private final LikesRepository likesRepository;

    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId) {
        return likesRepository.checkIsLikedPost(postIds, userId);
    }

    @Override
    public List<PostFetchDto> findLikedPosts(PostFetchCommand command) {
        return likesRepository.findLikedPosts(command);
    }
}
