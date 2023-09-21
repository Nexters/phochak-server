package com.nexters.phochak.post.application;

import com.nexters.phochak.post.application.port.in.ReportPostUseCase;
import com.nexters.phochak.post.application.port.out.CountReportPort;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.application.port.out.LoadUserPort;
import com.nexters.phochak.post.application.port.out.ReportNotifyPort;
import com.nexters.phochak.post.application.port.out.SavePostPort;
import com.nexters.phochak.post.application.port.out.SaveReportPostPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.ReportPost;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportPostService implements ReportPostUseCase {
    private final ReportNotifyPort reportNotifyPort;
    private final LoadPostPort loadPostPort;
    private final LoadUserPort loadUserPort;
    private final SaveReportPostPort saveReportPostPort;
    private final SavePostPort savePostPort;
    private final CountReportPort countReportPort;

    @Override
    @Transactional
    public void processReport(Long userId, Long postId) {
        User user = loadUserPort.load(userId);
        Post post = loadPostPort.load(postId);
        ReportPost reportPost = new ReportPost(user, post);
        saveReportPostPort.save(reportPost);

        int reportCount = countReportPort.count(post);
        if (reportPost.checkPenaltyToBlind(reportCount)) {
            post.blind();
        }
        savePostPort.save(post);
        reportNotifyPort.send(postId, userId, reportCount);
    }
}
