package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.post.adapter.out.persistence.HashtagEntity;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.domain.Post;

import java.util.List;
import java.util.Map;

public interface HashtagUseCase {
    List<HashtagEntity> saveHashtags(Post post, List<String> stringHashtagList);

    Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds);

    void updateAll(Post post, List<String> stringHashtagList);
}
