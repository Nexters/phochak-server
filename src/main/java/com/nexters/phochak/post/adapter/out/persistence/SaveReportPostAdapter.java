package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.application.port.out.SaveReportPostPort;
import com.nexters.phochak.post.domain.ReportPost;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveReportPostAdapter implements SaveReportPostPort {

    private final ReportPostMapper reportPostMapper;
    private final ReportPostRepository reportPostRepository;

    @Override
    public void save(final ReportPost reportPost) {
        ReportPostEntity reportPostEntity = reportPostMapper.toEntity(reportPost);
        try {
            reportPostRepository.save(reportPostEntity);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_REPORTED);
        }
    }
}
