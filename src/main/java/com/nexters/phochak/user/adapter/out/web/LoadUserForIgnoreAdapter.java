package com.nexters.phochak.user.adapter.out.web;

import com.nexters.phochak.user.application.port.out.LoadUserForIgnorePort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserForIgnoreAdapter implements LoadUserForIgnorePort {
    private final UserNetworkClient userNetworkClient;
    @Override
    public User loadUser(final Long myId) {
        return userNetworkClient.loadUser(myId);
    }
}
