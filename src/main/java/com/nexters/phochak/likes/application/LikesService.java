package com.nexters.phochak.likes.application;

import com.nexters.phochak.likes.LikesFetchDto;
import com.nexters.phochak.post.PostFetchCommand;
import com.nexters.phochak.post.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface LikesService {
    Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId);
    List<PostFetchDto> findLikedPostsByCommand(PostFetchCommand command);
    void addPhochak(Long userId, Long postId);
    void cancelPhochak(Long userId, Long postId);
}
