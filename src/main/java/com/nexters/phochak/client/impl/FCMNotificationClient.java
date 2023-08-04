package com.nexters.phochak.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.nexters.phochak.client.NotificationClient;
import com.nexters.phochak.dto.NotificationFormDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class FCMNotificationClient implements NotificationClient {

    private final ObjectMapper objectMapper;

    private final FirebaseMessaging fcm;

    @Async
    @Override
    public void postToClient(NotificationFormDto notificationFormDto, String registrationToken) {
        Message msg = Message.builder()
                .setToken(registrationToken)
                .putAllData(objectMapper.convertValue(notificationFormDto, Map.class))
                .build();
        try {
            fcm.send(msg);
        } catch (FirebaseMessagingException e) {
            log.error("[Internal Error Message] FCM 전송 실패", e);
            throw new PhochakException(ResCode.INTERNAL_SERVER_ERROR);
        }
    }
}
