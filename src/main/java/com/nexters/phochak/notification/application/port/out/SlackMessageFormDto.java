package com.nexters.phochak.notification.application.port.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageFormDto {
    private String username;
    private String text;
    private long count;
}
