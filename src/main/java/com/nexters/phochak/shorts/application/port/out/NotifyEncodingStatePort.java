package com.nexters.phochak.shorts.application.port.out;

import com.nexters.phochak.shorts.domain.ShortsStateEnum;

public interface NotifyEncodingStatePort {
    void postEncodeState(String uploadKey, ShortsStateEnum shortsStateEnum);
}
