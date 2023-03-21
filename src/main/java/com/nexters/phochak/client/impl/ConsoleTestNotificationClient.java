package com.nexters.phochak.client.impl;

import com.nexters.phochak.client.NotificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!prod")
@Component
public class ConsoleTestNotificationClient implements NotificationClient {
    @Override
    public void postToClient(String message, String registrationToken) {
        log.info("ConsoleTestNotificationClient|푸시 요청 테스트 발생: {}", message);
    }
}
