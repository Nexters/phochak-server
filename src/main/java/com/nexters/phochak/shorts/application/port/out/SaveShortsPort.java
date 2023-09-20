package com.nexters.phochak.shorts.application.port.out;

import com.nexters.phochak.shorts.domain.Shorts;

public interface SaveShortsPort {
    void save(Shorts shorts);
}
