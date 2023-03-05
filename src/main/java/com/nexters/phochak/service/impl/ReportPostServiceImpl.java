package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.ReportPost;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.ReportPostRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ReportPostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.PostBlockService;
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
    private final PostBlockService postBlockService;
    private final ReportPostRepository reportPostRepository;

    @Override
    @Transactional
    public void processReport(Long userId, Long postId, ReportPostRequestDto reportPostRequestDto) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.getReferenceById(postId);
        ReportPost reportPost = ReportPost.builder()
                .reporter(user)
                .post(post)
                .reason(reportPostRequestDto.getReason())
                .build();
        try {
            reportPostRepository.save(reportPost);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_REPORTED);
        }
        postBlockService.notifyAndBlockIfRequired(postId, userId, reportPost.getReason());
    }
}
