package com.nexters.phochak.service.impl;

import com.nexters.phochak.client.SlackPostReportFeignClient;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.ReportPost;
import com.nexters.phochak.dto.request.ReportPostRequestDto;
import com.nexters.phochak.repository.ReportPostRepository;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.SlackMessageFormDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostServiceImpl implements ReportPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportPostRepository reportPostRepository;
    private final SlackPostReportFeignClient slackPostReportFeignClient;

    @Value("${feign-client.slack.report.bot-nickname}")
    private String slackReportBotNickname;

    @Override
    @Transactional
    public void notifyReport(Long userId, Long postId, ReportPostRequestDto reportPostRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Post post = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        ReportPost reportPost = ReportPost.builder()
                .reporter(user)
                .post(post)
                .reason(reportPostRequestDto.getReason())
                .build();
        reportPostRepository.save(reportPost);

        String message = generateReportMessage(user, post, reportPostRequestDto.getReason());
        SlackMessageFormDto test = SlackMessageFormDto.builder()
                .username(slackReportBotNickname)
                .text(message)
                .build();
        slackPostReportFeignClient.call(test);
    }

    private String generateReportMessage(User user, Post post, String reason) {
        Long reportCount = reportPostRepository.countByPost(post);
        return "\uD83D\uDC6E\u200D 게시글 신고가 접수되었습니다 \uD83D\uDC6E\u200D"
                + "\n포스트 id: " + post.getId()
                + "\n누적 신고: " + reportCount
                + "\n신고자: " + user.getNickname()
                + "\n신고 사유: " + reason;
    }
}
