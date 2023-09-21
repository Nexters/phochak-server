package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.ReportPost;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportPostMapper {

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    public ReportPostEntity toEntity(final ReportPost reportPost) {
        return new ReportPostEntity(
            reportPost.getId(),
            userMapper.toEntity(reportPost.getUser()),
            postMapper.toEntity(reportPost.getPost()));
    }

    public ReportPost toDomain(final ReportPostEntity reportPostEntity) {
        return ReportPost.forMapper(
                reportPostEntity.getId(),
                userMapper.toDomain(reportPostEntity.getReporter()),
                postMapper.toDomain(reportPostEntity.getPost()));
    }

}
