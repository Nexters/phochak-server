package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.PostFetchDto;

import java.util.List;

public interface PostCustomRepository {
    List<PostFetchDto> findNextPageByCommmand(PostFetchCommand command);
}
