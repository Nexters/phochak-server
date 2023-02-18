package com.nexters.phochak.client;

import com.nexters.phochak.dto.SlackMessageFormDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ReportSlackNotificationFeignClient", url = "${feign-client.report-slack-web-hook-uri}")
public interface SlackPostReportFeignClient {

    @PostMapping
    String call(SlackMessageFormDto slackMessageFormDto);
}
