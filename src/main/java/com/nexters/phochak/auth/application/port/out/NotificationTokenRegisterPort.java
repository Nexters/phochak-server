package com.nexters.phochak.auth.application.port.out;

import com.nexters.phochak.notification.domain.OperatingSystem;
import com.nexters.phochak.user.domain.User;

public interface NotificationTokenRegisterPort {
    void register(User user, String token, OperatingSystem operatingSystem);
}
