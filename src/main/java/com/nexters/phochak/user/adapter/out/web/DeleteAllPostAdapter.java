package com.nexters.phochak.user.adapter.out.web;

import com.nexters.phochak.user.application.port.out.DeleteAllPostPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllPostAdapter implements DeleteAllPostPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void deleteAllPostByUser(final User user) {
        applicationEventPublisher.publishEvent(new DeleteAllPostEvent(user.getId()));
    }
}
