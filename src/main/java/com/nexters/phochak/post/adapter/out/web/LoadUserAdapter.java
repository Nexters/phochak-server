package com.nexters.phochak.post.adapter.out.web;

import com.nexters.phochak.post.application.port.out.LoadUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserAdapter implements LoadUserPort {
    private final LoadUserNetworkClient loadUserNetworkClient;

    @Override
    public User load(final Long userId) {
        return loadUserNetworkClient.load(userId);
    }
}
