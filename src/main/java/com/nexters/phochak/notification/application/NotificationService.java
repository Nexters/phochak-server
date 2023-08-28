package com.nexters.phochak.notification.application;

import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;

public interface NotificationService {
    void registryFcmDeviceToken(UserEntity userEntity, String fcmDeviceToken);

    void postEncodeState(String uploadKey, ShortsStateEnum shortsStateEnum);
}
