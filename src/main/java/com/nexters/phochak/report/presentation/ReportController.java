package com.nexters.phochak.report.presentation;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.post.application.port.in.CommonResponseDto;
import com.nexters.phochak.report.application.ReportPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/report")
public class ReportController {

    private final ReportPostService reportPostService;

    @Auth
    @PostMapping("/post/{postId}")
    public CommonResponseDto<Void> reportPost(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        reportPostService.processReport(userId, postId);
        return new CommonResponseDto<>();
    }
}
