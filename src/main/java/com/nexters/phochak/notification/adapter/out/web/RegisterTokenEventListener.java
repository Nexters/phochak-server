package com.nexters.phochak.notification.adapter.out.web;

import com.nexters.phochak.notification.application.port.in.NotificationUsecase;
import com.nexters.phochak.notification.application.port.in.RegisterTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterTokenEventListener {
    private final NotificationUsecase notificationUsecase;

    public void registerToken(final RegisterTokenRequest registerTokenRequest) {
        notificationUsecase.registryFcmDeviceToken(registerTokenRequest);
    }
}
