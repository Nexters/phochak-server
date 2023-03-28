package com.nexters.phochak.client;

import com.nexters.phochak.dto.NotificationFormDto;

public interface NotificationClient {
    void postToClient(NotificationFormDto notificationFormDto, String registrationToken);
}
