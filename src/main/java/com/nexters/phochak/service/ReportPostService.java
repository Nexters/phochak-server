package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.PostReportRequestDto;

public interface ReportPostService {
    void notifyReport(Long userId, Long postId, PostReportRequestDto postReportRequestDto);
}
