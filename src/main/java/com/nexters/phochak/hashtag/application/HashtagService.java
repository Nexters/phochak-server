package com.nexters.phochak.hashtag.application;

import com.nexters.phochak.hashtag.domain.Hashtag;
import com.nexters.phochak.hashtag.domain.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;

import java.util.List;
import java.util.Map;

public interface HashtagService {
    List<Hashtag> saveHashtagsByString(List<String> stringHashtagList, PostEntity postEntity);

    Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds);

    void updateAll(PostEntity postEntity, List<String> stringHashtagList);
}
