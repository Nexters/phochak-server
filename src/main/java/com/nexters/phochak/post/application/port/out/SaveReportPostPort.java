package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.ReportPost;

public interface SaveReportPostPort {
    void save(ReportPost reportPost);
}
