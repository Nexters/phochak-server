package com.nexters.phochak.service;

import com.nexters.phochak.dto.TokenDto;

public interface JwtTokenService {

    /**
     * user 정보를 담은 Access token을 만든다.
     * @param userId
     * @return
     */
    TokenDto generateAccessToken(Long userId);
}
