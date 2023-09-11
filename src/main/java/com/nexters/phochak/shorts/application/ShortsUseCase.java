package com.nexters.phochak.shorts.application;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.EncodingCallbackRequestDto;

public interface ShortsUseCase {

    void connectShorts(Post postEntity, String key);

    void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
