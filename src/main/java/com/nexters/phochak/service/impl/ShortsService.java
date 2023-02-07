package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.dto.request.PostCreateRequestDto;

public interface ShortsService {

    Shorts createShorts(PostCreateRequestDto postCreateRequestDto);
}
