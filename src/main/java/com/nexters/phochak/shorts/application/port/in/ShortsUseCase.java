package com.nexters.phochak.shorts.application.port.in;

import com.nexters.phochak.post.domain.Post;

public interface ShortsUseCase {

    void connectShorts(Post postEntity, String key);

    void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
