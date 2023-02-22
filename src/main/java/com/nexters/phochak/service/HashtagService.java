package com.nexters.phochak.service;

import com.nexters.phochak.domain.Hashtag;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.dto.HashtagFetchDto;

import java.util.List;
import java.util.Map;

public interface HashtagService {
    List<Hashtag> saveHashtagsByString(List<String> stringHashtagList, Post post);

    Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds);
}
