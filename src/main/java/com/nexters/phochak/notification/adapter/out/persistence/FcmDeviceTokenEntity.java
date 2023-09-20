package com.nexters.phochak.notification.adapter.out.persistence;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.notification.domain.OperatingSystem;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class FcmDeviceTokenEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="FCM_DEVICE_TOKEN_ID")
    private Long id;
    @JoinColumn(name = "USER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private OperatingSystem operatingSystem;

    public FcmDeviceTokenEntity(
            final Long id,
            final UserEntity user,
            final String token,
            final OperatingSystem operatingSystem) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.operatingSystem = operatingSystem;
    }

    public FcmDeviceTokenEntity() {
    }

}
