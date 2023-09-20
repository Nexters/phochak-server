package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.CountReportPort;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountReportAdapter implements CountReportPort {

    private final ReportPostRepository reportPostRepository;
    @Override
    public int count(final Post post) {
        return reportPostRepository.countByPostId(post.getId());
    }
}
