package com.nexters.phochak.notification.application;

import com.nexters.phochak.notification.application.port.out.DeviceInfo;
import com.nexters.phochak.notification.application.port.out.FindDeviceInfoPort;
import com.nexters.phochak.notification.application.port.out.NotificationUsecase;
import com.nexters.phochak.notification.application.port.out.RegisterFcmDeviceTokenPort;
import com.nexters.phochak.notification.application.port.out.RegisterTokenRequest;
import com.nexters.phochak.notification.application.port.out.SendNotificationPort;
import com.nexters.phochak.notification.domain.FcmDeviceToken;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements NotificationUsecase {
    private final RegisterFcmDeviceTokenPort registerFcmDeviceTokenPort;
    private final SendNotificationPort sendNotificationPort;
    private final FindDeviceInfoPort findDeviceInfoPort;

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
        DeviceInfo deviceInfo = findDeviceInfoPort.findByUploadKey(uploadKey);
        if(deviceInfo == null) {
            log.info("[NotificationServiceImpl|postEncodeState] FCM 미등록");
            return;
        }
        sendNotificationPort.postToClient(deviceInfo.postId(), shortsStateEnum, deviceInfo.deviceToken());
    }

}
