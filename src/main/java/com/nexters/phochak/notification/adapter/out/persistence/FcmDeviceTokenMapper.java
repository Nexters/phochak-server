package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.notification.domain.FcmDeviceToken;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FcmDeviceTokenMapper {
    private final UserMapper userMapper;

    public FcmDeviceToken toDomain(FcmDeviceTokenEntity fcmDeviceTokenEntity) {
        final FcmDeviceToken fcmDeviceToken = new FcmDeviceToken(
                userMapper.toDomain(fcmDeviceTokenEntity.getUserEntity()),
                fcmDeviceTokenEntity.getToken(),
                fcmDeviceTokenEntity.getOperatingSystem()
        );
        fcmDeviceToken.assignId(fcmDeviceTokenEntity.getId());
        return fcmDeviceToken;
    }

    public FcmDeviceTokenEntity toEntity(FcmDeviceToken fcmDeviceToken) {
        return new FcmDeviceTokenEntity(
                fcmDeviceToken.getId(),
                userMapper.toEntity(fcmDeviceToken.getUser()),
                fcmDeviceToken.getToken(),
                fcmDeviceToken.getOperatingSystem()
        );
    }
}
