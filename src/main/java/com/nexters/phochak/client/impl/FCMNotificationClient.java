package com.nexters.phochak.client.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.nexters.phochak.client.NotificationClient;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class FCMNotificationClient implements NotificationClient {

    private final FirebaseMessaging fcm;

    @Override
    public void postToClient(String message, String registrationToken) {
        Message msg = Message.builder()
                .setToken(registrationToken)
                .putData("body", message)
                .build();

        try {
            fcm.send(msg);
        } catch (FirebaseMessagingException e) {
            log.error("[Internal Error Message] FCM 전송 실패", e);
            throw new PhochakException(ResCode.INTERNAL_SERVER_ERROR);
        }
    }
}
