package com.nexters.phochak.report.presentation;

import com.nexters.phochak.report.SlackMessageFormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ReportNotificationFeignClient", url = "${slack.report.web-hook-uri}")
public interface ReportNotificationFeignClient {

    @PostMapping
    String call(SlackMessageFormDto slackMessageFormDto);
}
