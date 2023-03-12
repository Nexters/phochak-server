package com.nexters.phochak.service;

import org.springframework.scheduling.annotation.Async;

public interface NotifyService {
    @Async
    void notifyReportedPost(Long postId, Long userId, String reason, Long reportCount);
}
