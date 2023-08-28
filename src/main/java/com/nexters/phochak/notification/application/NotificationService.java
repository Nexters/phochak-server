package com.nexters.phochak.notification.application;

import com.nexters.phochak.notification.adapter.out.api.NotificationClient;
import com.nexters.phochak.notification.adapter.out.api.NotificationFormDto;
import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenRepository;
import com.nexters.phochak.notification.application.port.in.NotificationUsecase;
import com.nexters.phochak.notification.application.port.in.RegisterTokenRequest;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements NotificationUsecase {
    private final FcmDeviceTokenRepository fcmDeviceTokenRepository;
    private final NotificationClient notificationClient;

    @Override
    public void registryFcmDeviceToken(RegisterTokenRequest registerTokenRequest) {

        fcmDeviceTokenRepository.save(
                FcmDeviceTokenEntity.builder()
                    .userEntity(userEntity)
                    .token(token)
                    .build());
    }

    @Override
    public void postEncodeState(String uploadKey, ShortsStateEnum shortsStateEnum) {
        List<Object[]> queryResult = fcmDeviceTokenRepository.findDeviceTokenAndPostIdByUploadKey(uploadKey);

        if(queryResult.isEmpty()) {
            log.info("[NotificationServiceImpl|postEncodeState] FCM 미등록");
//            log.error("[NotificationServiceImpl|postEncodeState] UploadKey 와 FCM 토큰 매칭 실패. 업로드 키: {}", uploadKey);
//            throw new PhochakException(ResCode.INTERNAL_SERVER_ERROR);
            return; // 클라이언트 V2 구현 전까지 처리 보류
        }

        String deviceToken = queryResult.get(0)[0].toString();
        Long postId = Long.valueOf(String.valueOf(queryResult.get(0)[1]));

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
