package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.post.adapter.out.persistence.PostFetchCommand;

import java.util.List;
import java.util.Map;

public interface LikesUseCase {
    Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId);
    List<PostFetchDto> findLikedPostsByCommand(PostFetchCommand command);
    void addPhochak(Long userId, Long postId);
    void cancelPhochak(Long userId, Long postId);
}
