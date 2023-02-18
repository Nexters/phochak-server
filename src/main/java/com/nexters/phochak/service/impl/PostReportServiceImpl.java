package com.nexters.phochak.service.impl;

import com.nexters.phochak.client.SlackPostReportFeignClient;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.ReportPost;
import com.nexters.phochak.domain.ReportPostRepository;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.SlackMessageFormDto;
import com.nexters.phochak.dto.request.PostReportRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.PostReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostReportServiceImpl implements PostReportService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportPostRepository reportPostRepository;
    private final SlackPostReportFeignClient slackPostReportFeignClient;

    @Value("${feign-client.slack.report.bot-nickname}")
    private final String slackReportBotNickname;

    @Override
    @Transactional
    public void notifyReport(Long userId, Long postId, PostReportRequestDto postReportRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Post post = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        ReportPost reportPost = ReportPost.builder()
                .reporter(user)
                .post(post)
                .reason(postReportRequestDto.getReason())
                .build();
        reportPostRepository.save(reportPost);
        String message = createPostReportMessage(user, post, postReportRequestDto.getReason());
        SlackMessageFormDto test = SlackMessageFormDto.builder()
                .username(slackReportBotNickname)
                .text(message)
                .build();
        slackPostReportFeignClient.call(test);
    }

    private String createPostReportMessage(User user, Post post, String reason) {
        Long reportCount = reportPostRepository.countByPost(post);
        return "게시글 신고가 접수되었습니다."
                + "\n신고자: " + user.getNickname()
                + "\n포스트 id: " + post.getId()
                + "\n누적 신고: " + reportCount;
    }
}
