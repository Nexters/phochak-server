package com.nexters.phochak.service;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;

public interface ShortsService {

    void connectShorts(String key, Post post);

    void connectPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
