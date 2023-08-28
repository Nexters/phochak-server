package com.nexters.phochak.auth.adapter.out.web;

import com.nexters.phochak.notification.application.port.out.RegisterTokenRequest;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationTokenRegisterMonolithicClient implements NotificationTokenRegisterNetworkClient {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void register(final User user, final String token) {
        eventPublisher.publishEvent(new NotificationTokenRegisterOutEvent(new RegisterTokenRequest(user, token)));
    }
}
