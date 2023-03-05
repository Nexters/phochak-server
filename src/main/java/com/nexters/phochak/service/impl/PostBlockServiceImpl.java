package com.nexters.phochak.service.impl;

import com.nexters.phochak.client.SlackPostReportFeignClient;
import com.nexters.phochak.config.property.SlackReportProperties;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.dto.SlackMessageFormDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ReportPostRepository;
import com.nexters.phochak.service.PostBlockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
@Service
public class PostBlockServiceImpl implements PostBlockService {
    private static final Long BLOCK_CRITERIA = 20L;
    private final ReportPostRepository reportPostRepository;
    private final SlackPostReportFeignClient slackPostReportFeignClient;
    private final PostRepository postRepository;
    private final SlackReportProperties slackReportProperties;

    @Async
    @Override
    public void notifyAndBlockIfRequired(Long postId, Long userId, String reason) {
        try {
            Long reportCount = reportPostRepository.countByPost_Id(postId);
            if (reportCount >= BLOCK_CRITERIA) {
                Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
                post.blindPost();
            }

            String message = generateReportMessage(userId, postId, reason, reportCount);
            SlackMessageFormDto test = SlackMessageFormDto.builder()
                    .username(slackReportProperties.getBotNickname())
                    .text(message)
                    .build();
            slackPostReportFeignClient.call(test);
        } catch (Exception e) {
            log.error("[PostBlockServiceImpl|Fail] 게시글 블라인드 처리 시 예외 발생", e);
        }
    }

    private String generateReportMessage(Long userId, Long postId, String reason, long reportCount) {
        StringBuilder sb = new StringBuilder();

        sb.append("\uD83D\uDC6E\u200D 게시글 신고가 접수되었습니다 \uD83D\uDC6E\u200D")
                .append("\n포스트 id: ")
                .append(postId)
                .append("\n누적 신고: ")
                .append(reportCount)
                .append("\n신고자 id: ")
                .append(userId)
                .append("\n신고 사유: ")
                .append(reason)
                .append("\n누적 신고 횟수: ")
                .append(reportCount);

        return sb.toString();
    }
}
