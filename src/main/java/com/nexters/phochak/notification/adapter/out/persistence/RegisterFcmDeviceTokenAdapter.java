package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.notification.application.port.out.RegisterFcmDeviceTokenPort;
import com.nexters.phochak.notification.domain.FcmDeviceToken;

public class RegisterFcmDeviceTokenAdapter implements RegisterFcmDeviceTokenPort {
    @Override
    public void save(final FcmDeviceToken fcmDeviceToken) {
        throw new UnsupportedOperationException("RegisterFcmDeviceTokenAdapter#save not implemented yet.")
    }
}
