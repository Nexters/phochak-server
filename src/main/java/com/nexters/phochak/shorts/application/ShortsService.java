package com.nexters.phochak.shorts.application;

import com.nexters.phochak.post.adapter.out.persistence.Post;
import com.nexters.phochak.shorts.EncodingCallbackRequestDto;

public interface ShortsService {

    void connectShorts(String key, Post post);

    void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
