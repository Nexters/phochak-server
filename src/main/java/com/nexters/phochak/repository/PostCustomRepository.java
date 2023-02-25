package com.nexters.phochak.repository;

import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;

import java.util.List;

public interface PostCustomRepository {
    List<PostFetchDto> findNextPageByCommmand(PostFetchCommand command);
}
