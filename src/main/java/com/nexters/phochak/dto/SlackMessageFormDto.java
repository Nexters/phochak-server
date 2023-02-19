package com.nexters.phochak.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageFormDto {
    private String username;
    private String text;
}
