package com.nexters.phochak.report.presentation;

import org.springframework.scheduling.annotation.Async;

public interface ReportNotifyService {
    @Async
    void notifyReportedPost(Long postId, Long userId, Long reportCount);
}
