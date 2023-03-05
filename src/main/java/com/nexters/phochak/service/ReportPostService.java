package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.ReportPostRequestDto;

public interface ReportPostService {
    void processReport(Long userId, Long postId, ReportPostRequestDto postReportRequestDto);
}
