package com.nexters.phochak.user.adapter.out.event;

import com.nexters.phochak.user.application.port.out.DeleteAllPostPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllPostAdapter implements DeleteAllPostPort {

    private final DeleteAllPostNetworkClient deleteAllPostNetworkClient;

    @Override
    public void deleteAllPostByUser(final User user) {
        deleteAllPostNetworkClient.deleteAllPostByUser(user);
    }
}
