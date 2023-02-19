package com.nexters.phochak.service;

import com.nexters.phochak.dto.request.ReportPostRequestDto;

public interface ReportPostService {
    void notifyReport(Long userId, Long postId, ReportPostRequestDto postReportRequestDto);
}
