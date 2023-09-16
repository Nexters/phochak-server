package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface HashtagCustomRepository {
    Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds);

    List<PostFetchDto> findSearchedPageByCommmand(PostFetchCommand command);
}