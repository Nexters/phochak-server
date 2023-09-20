package com.nexters.phochak.notification.application.port.out;

public interface FindDeviceInfoPort {
    DeviceInfo findByUploadKey(String uploadKey);
}
