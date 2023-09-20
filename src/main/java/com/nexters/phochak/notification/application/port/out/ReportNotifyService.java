package com.nexters.phochak.notification.application.port.out;

import org.springframework.scheduling.annotation.Async;

public interface ReportNotifyService {
    @Async
    void notifyReportedPost(Long postId, Long userId, int reportCount);
}
