package com.nexters.phochak.user.adapter.out.event;

import com.nexters.phochak.notification.domain.OperatingSystem;
import com.nexters.phochak.user.domain.User;

public interface NotificationTokenRegisterNetworkClient {
    void register(final User user, final String token, final OperatingSystem operatingSystem);
}
