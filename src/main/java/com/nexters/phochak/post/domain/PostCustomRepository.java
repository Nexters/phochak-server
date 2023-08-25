package com.nexters.phochak.post.domain;

import com.nexters.phochak.post.PostFetchCommand;
import com.nexters.phochak.post.PostFetchDto;

import java.util.List;

public interface PostCustomRepository {
    List<PostFetchDto> findNextPageByCommmand(PostFetchCommand command);
}
