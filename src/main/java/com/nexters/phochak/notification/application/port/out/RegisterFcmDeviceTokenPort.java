package com.nexters.phochak.notification.application.port.out;

import com.nexters.phochak.notification.domain.FcmDeviceToken;

public interface RegisterFcmDeviceTokenPort {
    void save(final FcmDeviceToken fcmDeviceToken);
}
