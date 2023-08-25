package com.nexters.phochak.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class NotificationFormDto {

    private String type;

    private String target;

    private String title;

    private String content;
}
