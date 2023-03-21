package com.nexters.phochak.client;

public interface NotificationClient {
    void postToClient(String message, String registrationToken);
}
