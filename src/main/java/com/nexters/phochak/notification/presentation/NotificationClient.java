package com.nexters.phochak.notification.presentation;

import com.nexters.phochak.notification.NotificationFormDto;

public interface NotificationClient {
    void postToClient(NotificationFormDto notificationFormDto, String registrationToken);
}
