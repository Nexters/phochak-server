package com.nexters.phochak.notification.application.port.out;

import com.nexters.phochak.shorts.domain.ShortsStateEnum;

public interface SendNotificationPort {
    void postToClient(Long postId, ShortsStateEnum shortsStateEnum, String deviceToken);
}
