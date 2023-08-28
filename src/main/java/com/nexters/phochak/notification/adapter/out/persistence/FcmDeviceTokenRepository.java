package com.nexters.phochak.notification.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FcmDeviceTokenRepository extends JpaRepository<FcmDeviceTokenEntity, Long> {

    @Query("SELECT T.token, P.id FROM Post P JOIN P.user U JOIN U.fcmDeviceToken T JOIN P.shorts S WHERE S.uploadKey = :uploadKey")
    List<Object[]> findDeviceTokenAndPostIdByUploadKey(@Param("uploadKey") String uploadKey);
}
