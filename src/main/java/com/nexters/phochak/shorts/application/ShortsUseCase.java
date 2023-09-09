package com.nexters.phochak.shorts.application;

import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.shorts.EncodingCallbackRequestDto;

public interface ShortsUseCase {

    void connectShorts(String key, PostEntity postEntity);

    void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
