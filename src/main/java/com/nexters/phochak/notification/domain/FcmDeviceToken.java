package com.nexters.phochak.notification.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.user.domain.User;
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
    private User user;

    @Column(nullable = false)
    private String token;

    @Builder
    public FcmDeviceToken(Long id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public FcmDeviceToken() {
    }

    public void updateDeviceToken(String token) {
        this.token = token;
    }
}
