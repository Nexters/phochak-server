package com.nexters.phochak.auth.adapter.out.web;

import com.nexters.phochak.auth.application.port.out.NotificationTokenRegisterPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationTokenRegisterAdapter implements NotificationTokenRegisterPort {

    private final NotificationTokenRegisterNetworkClient notificationTokenRegisterNetworkClient;

    @Override
    public void register(final User user, final String token) {
        notificationTokenRegisterNetworkClient.register(user, token);
    }
}
