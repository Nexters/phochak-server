package com.nexters.phochak.common.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "slack.report")
public class SlackReportProperties {
    private final String botNickname;
    private final String webHookUri;
}
