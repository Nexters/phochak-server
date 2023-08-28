package com.nexters.phochak.auth.application.port.out;

public interface NotificationTokenRegisterPort {
    void register(Long userId, String token);
}
