package com.nexters.phochak.post.adapter.out.api;

import com.nexters.phochak.common.config.property.SlackReportProperties;
import com.nexters.phochak.post.application.port.out.ReportNotifyPort;
import com.nexters.phochak.post.application.port.out.SlackMessageFormDto;
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
public class ReportPostNotifyAdapter implements ReportNotifyPort {
    private final ReportNotificationFeignClient slackPostReportFeignClient;
    private final SlackReportProperties slackReportProperties;

    @Async
    @Override
    public void send(Long postId, Long userId, int reportCount) {
        try {
            String message = generateReportMessage(userId, postId, reportCount);
            SlackMessageFormDto test = SlackMessageFormDto.builder()
                    .username(slackReportProperties.getBotNickname())
                    .text(message)
                    .build();
            slackPostReportFeignClient.call(test);
        } catch (Exception e) {
            log.error("[PostBlockServiceImpl|Fail] 게시글 블라인드 처리 시 예외 발생", e);
        }
    }

    private String generateReportMessage(Long userId, Long postId, long reportCount) {
        StringBuilder sb = new StringBuilder();

        sb.append("\uD83D\uDC6E\u200D 게시글 신고가 접수되었습니다 \uD83D\uDC6E\u200D")
                .append("\n포스트 id: ")
                .append(postId)
                .append("\n누적 신고: ")
                .append(reportCount)
                .append("\n신고자 id: ")
                .append(userId)
                .append("\n누적 신고 횟수: ")
                .append(reportCount);

        return sb.toString();
    }
}
