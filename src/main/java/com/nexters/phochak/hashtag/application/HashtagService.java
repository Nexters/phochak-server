package com.nexters.phochak.hashtag.application;

import com.nexters.phochak.hashtag.domain.Hashtag;
import com.nexters.phochak.hashtag.domain.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.Post;

import java.util.List;
import java.util.Map;

public interface HashtagService {
    List<Hashtag> saveHashtagsByString(List<String> stringHashtagList, Post post);

    Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds);

    void updateAll(Post post, List<String> stringHashtagList);
}
