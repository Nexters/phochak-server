package com.nexters.phochak.ignore.adapter.out.web;

import com.nexters.phochak.ignore.application.port.out.LoadIgnoredUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadIgnoredUserAdapter implements LoadIgnoredUserPort {
    private final UserNetworkClient userNetworkClient;
    @Override
    public User loadIgnoredUser(final Long myId) {
        return userNetworkClient.getUser(myId);
    }
}
