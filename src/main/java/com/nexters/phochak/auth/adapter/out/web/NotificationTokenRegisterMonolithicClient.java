package com.nexters.phochak.auth.adapter.out.web;

import com.nexters.phochak.notification.application.port.in.RegisterTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationTokenRegisterMonolithicClient implements NotificationTokenRegisterNetworkClient {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void register(final Long userId, final String token) {
        eventPublisher.publishEvent(new NotificationTokenRegisterOutEvent(new RegisterTokenRequest(userId, token)));
    }
}
