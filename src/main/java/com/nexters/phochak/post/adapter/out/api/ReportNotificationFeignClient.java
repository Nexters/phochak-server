package com.nexters.phochak.post.adapter.out.api;

import com.nexters.phochak.notification.application.port.out.SlackMessageFormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ReportNotificationFeignClient", url = "${slack.report.web-hook-uri}")
public interface ReportNotificationFeignClient {

    @PostMapping
    String call(SlackMessageFormDto slackMessageFormDto);
}
