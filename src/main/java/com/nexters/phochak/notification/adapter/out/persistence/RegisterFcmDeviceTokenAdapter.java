package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.notification.application.port.out.RegisterFcmDeviceTokenPort;
import com.nexters.phochak.notification.domain.FcmDeviceToken;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class RegisterFcmDeviceTokenAdapter implements RegisterFcmDeviceTokenPort {
    private final FcmDeviceTokenRepository fcmDeviceTokenRepository;
    private final FcmDeviceTokenMapper fcmDeviceTokenMapper;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void saveOrUpdate(final FcmDeviceToken fcmDeviceToken) {
        final UserEntity userEntity = userMapper.toEntity(fcmDeviceToken.getUser());
        if (alreadyExistDevice(fcmDeviceToken, userEntity)) {
            updateToken(fcmDeviceToken, userEntity);
        } else {
            final FcmDeviceTokenEntity fcmDeviceTokenEntity = fcmDeviceTokenMapper.toEntity(fcmDeviceToken);
            fcmDeviceTokenRepository.save(fcmDeviceTokenEntity);
        }
    }

    private void updateToken(final FcmDeviceToken fcmDeviceToken, final UserEntity userEntity) {
        fcmDeviceTokenRepository.updateTokenByUserEntityAndOperatingSystem(
                fcmDeviceToken.getToken(),
                userEntity,
                fcmDeviceToken.getOperatingSystem());
    }

    private boolean alreadyExistDevice(final FcmDeviceToken fcmDeviceToken, final UserEntity userEntity) {
        return fcmDeviceTokenRepository.existsByUserAndOperatingSystem(
                userEntity,
                fcmDeviceToken.getOperatingSystem());
    }
}
