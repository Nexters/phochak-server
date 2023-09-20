package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.application.port.in.LikesFetchDto;

import java.util.List;
import java.util.Map;

public interface CheckLikedPort {
    Map<Long, LikesFetchDto> checkIsLikedPostList(List<Long> postIds, Long userId);
}
