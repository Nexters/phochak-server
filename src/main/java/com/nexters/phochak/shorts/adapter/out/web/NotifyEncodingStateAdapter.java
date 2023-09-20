package com.nexters.phochak.shorts.adapter.out.web;

import com.nexters.phochak.shorts.application.port.out.NotifyEncodingStatePort;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifyEncodingStateAdapter implements NotifyEncodingStatePort {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Async
    public void postEncodeState(final String uploadKey, final ShortsStateEnum shortsStateEnum) {
        applicationEventPublisher.publishEvent(new NotifyEncodingStateEvent(uploadKey, shortsStateEnum));
    }
}
