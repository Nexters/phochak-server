package com.nexters.phochak.notification.adapter.out.api;

public interface NotificationClient {
    void postToClient(NotificationFormDto notificationFormDto, String registrationToken);
}
