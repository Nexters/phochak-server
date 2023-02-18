package com.nexters.phochak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ReportSlackNotificationFeignClient", url = "${feign-client.report-slack-web-hook-uri}")
public interface SlackPostReportFeignClient {

    @GetMapping
    String call();
}
