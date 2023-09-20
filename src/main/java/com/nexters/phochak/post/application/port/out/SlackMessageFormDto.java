package com.nexters.phochak.post.application.port.out;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageFormDto {
    private String username;
    private String text;
    private long count;
}
