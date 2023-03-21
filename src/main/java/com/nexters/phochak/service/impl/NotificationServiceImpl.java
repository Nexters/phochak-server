package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.FcmDeviceToken;
import com.nexters.phochak.repository.FcmDeviceTokenRepository;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.service.NotificationService;
import com.nexters.phochak.specification.ShortsStateEnum;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final FcmDeviceTokenRepository fcmDeviceTokenRepository;

    public NotificationServiceImpl(FcmDeviceTokenRepository fcmDeviceTokenRepository) {
        this.fcmDeviceTokenRepository = fcmDeviceTokenRepository;
    }

    @Override
    public void registryFcmDeviceToken(User user, String token) {
        fcmDeviceTokenRepository.save(
                FcmDeviceToken.builder()
                    .user(user)
                    .token(token)
                    .build());
    }

    @Override
    public void postEncodeState(String uploadKey, ShortsStateEnum shortsStateEnum) {

    }
}
