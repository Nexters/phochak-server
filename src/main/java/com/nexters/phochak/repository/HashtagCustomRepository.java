package com.nexters.phochak.repository;

import com.nexters.phochak.dto.HashtagFetchDto;

import java.util.List;
import java.util.Map;

public interface HashtagCustomRepository {
    Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds);
}
