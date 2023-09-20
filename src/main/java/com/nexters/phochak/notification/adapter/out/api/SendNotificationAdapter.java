package com.nexters.phochak.notification.adapter.out.api;

import com.nexters.phochak.notification.application.port.out.SendNotificationPort;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendNotificationAdapter implements SendNotificationPort {

    private NotificationClient notificationClient;

    @Override
    public void postToClient(final Long postId, final ShortsStateEnum shortsStateEnum, final String deviceToken) {
        notificationClient.postToClient(getEncodeStateMessage(postId, shortsStateEnum), deviceToken);
    }

    private NotificationFormDto getEncodeStateMessage(Long postId, ShortsStateEnum shortsStateEnum) {
        return NotificationFormDto.builder()
                .title("게시글 업로드 상태")
                .content("게시글 업로드 상태")
                .type(shortsStateEnum.toString())
                .target(postId.toString())
                .build();
    }
}
