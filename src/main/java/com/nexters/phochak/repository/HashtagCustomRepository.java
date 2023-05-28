package com.nexters.phochak.repository;

import com.nexters.phochak.dto.HashtagFetchDto;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;

import java.util.List;
import java.util.Map;

public interface HashtagCustomRepository {
    Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds);

    List<PostFetchDto> findSearchedPageByCommmand(PostFetchCommand command);
}
