package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.ReportPost;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ReportPostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.NotifyService;
import com.nexters.phochak.service.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostServiceImpl implements ReportPostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotifyService notifyService;
    private final ReportPostRepository reportPostRepository;

    @Override
    @Transactional
    public void processReport(Long userId, Long postId) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.getReferenceById(postId);
        ReportPost reportPost = ReportPost.builder()
                .reporter(user)
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
        notifyService.notifyReportedPost(postId, userId, reportCount);
    }
}
