package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.PostReportRequestDto;

public interface PostReportService {
    void notifyReport(Long userId, Long postId, PostReportRequestDto postReportRequestDto);
}
