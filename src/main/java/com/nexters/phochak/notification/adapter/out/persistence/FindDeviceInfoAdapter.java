package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.notification.application.port.out.DeviceInfo;
import com.nexters.phochak.notification.application.port.out.FindDeviceInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindDeviceInfoAdapter implements FindDeviceInfoPort {

    private final FcmDeviceTokenRepository fcmDeviceTokenRepository;

    @Override
    public DeviceInfo findByUploadKey(final String uploadKey) {
        List<Object[]> queryResult = fcmDeviceTokenRepository.findDeviceTokenAndPostIdByUploadKey(uploadKey);
        if (queryResult.isEmpty()) {
            return null;
        }
        return new DeviceInfo(Long.valueOf(String.valueOf(queryResult.get(0)[0])), queryResult.get(0)[1].toString());
    }
}
