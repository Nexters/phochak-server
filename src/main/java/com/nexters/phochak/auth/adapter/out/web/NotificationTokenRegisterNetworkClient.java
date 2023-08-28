package com.nexters.phochak.auth.adapter.out.web;

import com.nexters.phochak.user.domain.User;

public interface NotificationTokenRegisterNetworkClient {
    void register(final User user, final String token);
}
