package com.nexters.phochak.hashtag.domain;

import com.nexters.phochak.post.PostFetchCommand;
import com.nexters.phochak.post.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface HashtagCustomRepository {
    Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds);

    List<PostFetchDto> findSearchedPageByCommmand(PostFetchCommand command);
}
