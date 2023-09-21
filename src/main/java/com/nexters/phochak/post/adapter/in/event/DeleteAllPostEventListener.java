package com.nexters.phochak.post.adapter.in.event;

import com.nexters.phochak.post.application.port.in.PostUseCase;
import com.nexters.phochak.user.adapter.out.event.DeleteAllPostEvent;
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
    public void deletion(final DeleteAllPostEvent event) {
        postUseCase.deleteAllPost(event.id());
    }
}
