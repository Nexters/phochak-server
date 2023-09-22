package com.nexters.phochak.user.adapter.out.event;

import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteAllPostMonolithicClient implements DeleteAllPostNetworkClient {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void deleteAllPostByUser(final User user) {
        applicationEventPublisher.publishEvent(new DeleteAllPostEvent(user.getId()));
    }
}
