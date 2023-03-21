package com.nexters.phochak.service;

import com.nexters.phochak.domain.User;

public interface NotificationService {
    void registryFcmDeviceToken(User user, String fcmDeviceToken);
}
