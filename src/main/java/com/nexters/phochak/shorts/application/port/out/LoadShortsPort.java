package com.nexters.phochak.shorts.application.port.out;

import com.nexters.phochak.shorts.domain.Shorts;

public interface LoadShortsPort {
    Shorts findByUploadKey(String uploadKey);
}
