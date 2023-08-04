package com.nexters.phochak.service;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface ShortsService {

    void connectShorts(String key, Post post);

    void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto);
}
