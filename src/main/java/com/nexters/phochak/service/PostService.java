package com.nexters.phochak.service;

import com.nexters.phochak.dto.PostCreateRequestDto;

public interface PostService {

    void create(Long userId, PostCreateRequestDto postCreateRequestDto);
}
