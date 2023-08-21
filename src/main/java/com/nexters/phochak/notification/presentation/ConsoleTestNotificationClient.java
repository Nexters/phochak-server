package com.nexters.phochak.notification.presentation;

import com.nexters.phochak.notification.NotificationFormDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!prod")
@Component
public class ConsoleTestNotificationClient implements NotificationClient {
    @Override
    public void postToClient(NotificationFormDto notificationFormDto, String registrationToken) {
        log.info("[ConsoleTestNotificationClient|postToClient]푸시 요청 테스트 발생: {}", notificationFormDto.toString());
    }
}
