package com.nexters.phochak.likes.domain;

import com.nexters.phochak.likes.LikesFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.PostFetchCommand;
import com.nexters.phochak.post.application.port.in.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface LikesCustomRepository {
    Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId);

    List<PostFetchDto> findLikedPosts(PostFetchCommand command);
}
