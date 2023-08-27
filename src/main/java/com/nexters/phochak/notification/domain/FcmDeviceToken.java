package com.nexters.phochak.notification.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class FcmDeviceToken extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="FCM_DEVICE_TOKEN_ID")
    private Long id;

    @JoinColumn(name = "USER_ID")
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    @Column(nullable = false)
    private String token;

    @Builder
    public FcmDeviceToken(Long id, UserEntity userEntity, String token) {
        this.id = id;
        this.userEntity = userEntity;
        this.token = token;
    }

    public FcmDeviceToken() {
    }

    public void updateDeviceToken(String token) {
        this.token = token;
    }
}
