package com.nexters.phochak.post.application.port.out;

import org.springframework.scheduling.annotation.Async;

public interface ReportNotifyPort {
    @Async
    void send(Long postId, Long userId, int reportCount);
}
