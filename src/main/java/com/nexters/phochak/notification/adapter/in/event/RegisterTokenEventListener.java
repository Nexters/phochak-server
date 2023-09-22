package com.nexters.phochak.notification.adapter.in.event;

import com.nexters.phochak.notification.application.port.out.NotificationUsecase;
import com.nexters.phochak.notification.application.port.out.RegisterTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterTokenEventListener {
    private final NotificationUsecase notificationUsecase;

    @EventListener
    public void registerToken(final RegisterTokenRequest registerTokenRequest) {
        notificationUsecase.registryFcmDeviceToken(registerTokenRequest);
    }
}
