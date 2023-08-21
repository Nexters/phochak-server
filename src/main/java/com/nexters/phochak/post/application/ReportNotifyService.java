package com.nexters.phochak.post.application;

import org.springframework.scheduling.annotation.Async;

public interface ReportNotifyService {
    @Async
    void notifyReportedPost(Long postId, Long userId, Long reportCount);
}
