package com.nexters.phochak.post.application.port.in;

public interface ReportPostUseCase {
    void processReport(Long userId, Long postId);
}
