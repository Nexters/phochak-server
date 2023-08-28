package com.nexters.phochak.report.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.application.ReportNotifyService;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.report.domain.ReportPost;
import com.nexters.phochak.report.domain.ReportPostRepository;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostServiceImpl implements ReportPostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportNotifyService reportNotifyService;
    private final ReportPostRepository reportPostRepository;

    @Override
    @Transactional
    public void processReport(Long userId, Long postId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        Post post = postRepository.getReferenceById(postId);
        ReportPost reportPost = ReportPost.builder()
                .reporter(userEntity)
                .post(post)
                .build();
        try {
            reportPostRepository.save(reportPost);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_REPORTED);
        }

        Long reportCount = reportPostRepository.countByPost_Id(postId);
        post.blindPostIfRequired(reportCount);

        // 슬랙알림 전송
        reportNotifyService.notifyReportedPost(postId, userId, reportCount);
    }
}
