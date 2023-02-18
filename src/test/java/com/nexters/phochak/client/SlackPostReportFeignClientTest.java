package com.nexters.phochak.client;

import com.nexters.phochak.dto.SlackMessageFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SlackPostReportFeignClientTest {

    @Autowired SlackPostReportFeignClient slackPostReportFeignClient;

    @Test
    void test() {
        SlackMessageFormDto test = SlackMessageFormDto.builder()
                .username("알람봇~")
                .text("안녕")
                .build();
        slackPostReportFeignClient.call(test);
    }
}