package com.nexters.phochak.post.application.port.in;

import java.util.List;
import java.util.Map;

public interface LikesUseCase {
    Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId);
    void addPhochak(Long userId, Long postId);
    void cancelPhochak(Long userId, Long postId);
}
