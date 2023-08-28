package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.notification.domain.OperatingSystem;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FcmDeviceTokenRepository extends JpaRepository<FcmDeviceTokenEntity, Long> {

    @Query("SELECT T.token, P.id FROM Post P JOIN P.user U JOIN U.fcmDeviceToken T JOIN P.shorts S WHERE S.uploadKey = :uploadKey")
    List<Object[]> findDeviceTokenAndPostIdByUploadKey(@Param("uploadKey") String uploadKey);

    Boolean existsByUserAndOperatingSystem(UserEntity userEntity, OperatingSystem operatingSystem);

    @Modifying
    @Query("update FcmDeviceTokenEntity T set T.token = :token where T.user = :user and T.operatingSystem = :operatingSystem")
    void updateTokenByUserEntityAndOperatingSystem(@Param("token") String token, @Param("user") UserEntity user, @Param("operatingSystem")OperatingSystem operatingSystem);
}
