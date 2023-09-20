package com.nexters.phochak.notification.adapter.out.web;

import com.nexters.phochak.notification.application.port.out.NotificationUsecase;
import com.nexters.phochak.shorts.adapter.out.web.NotifyEncodingStateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEncodingEventListener {

    private final NotificationUsecase notificationUsecase;

    @EventListener
    public void registerToken(final NotifyEncodingStateEvent event) {
        notificationUsecase.postEncodeState(event.uploadKey(), event.shortsStateEnum());
    }
}
