package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.post.adapter.out.persistence.Hashtag;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;

import java.util.List;
import java.util.Map;

public interface HashtagUseCase {
    List<Hashtag> saveHashtagsByString(List<String> stringHashtagList, PostEntity postEntity);

    Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds);

    void updateAll(PostEntity postEntity, List<String> stringHashtagList);
}
