package com.nexters.phochak.post.adapter.out.web;

import com.nexters.phochak.post.application.port.in.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAllPostEventListener {

    private final PostUseCase postUseCase;

    @Async
    @EventListener
    public void deletion(final Long userId) {
        postUseCase.deleteAllPost(userId);
    }
}
