package com.nexters.phochak.notification.application;

import com.nexters.phochak.notification.application.port.out.NotificationUsecase;
import com.nexters.phochak.notification.application.port.out.RegisterFcmDeviceTokenPort;
import com.nexters.phochak.notification.application.port.out.RegisterTokenRequest;
import com.nexters.phochak.notification.application.port.out.SendNotificationPort;
import com.nexters.phochak.notification.domain.FcmDeviceToken;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements NotificationUsecase {
//    private final FcmDeviceTokenRepository fcmDeviceTokenRepository;
    private final RegisterFcmDeviceTokenPort registerFcmDeviceTokenPort;
    private final SendNotificationPort sendNotificaitonPort;
//    private final NotificationClient notificationClient;

    @Override
    public void registryFcmDeviceToken(RegisterTokenRequest registerTokenRequest) {
        FcmDeviceToken fcmDeviceToken = new FcmDeviceToken(
                registerTokenRequest.user(),
                registerTokenRequest.token(),
                registerTokenRequest.operatingSystem());
        registerFcmDeviceTokenPort.saveOrUpdate(fcmDeviceToken);
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

        sendNotificaitonPort.postToClient(postId, shortsStateEnum, deviceToken);
    }


}
