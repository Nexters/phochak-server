package com.nexters.phochak.notification.application.port.out;

import com.nexters.phochak.shorts.domain.ShortsStateEnum;

public interface NotificationUsecase {
    void registryFcmDeviceToken(RegisterTokenRequest registerTokenRequest);

    void postEncodeState(String uploadKey, ShortsStateEnum shortsStateEnum);
}
