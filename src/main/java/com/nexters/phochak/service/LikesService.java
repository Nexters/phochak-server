package com.nexters.phochak.service;

import com.nexters.phochak.dto.LikesFetchDto;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface LikesService {
    Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId);

    List<PostFetchDto> findLikedPosts(PostFetchCommand command);
}
