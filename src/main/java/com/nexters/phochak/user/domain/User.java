package com.nexters.phochak.user.domain;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.notification.domain.FcmDeviceToken;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseTime {
    public static final int NICKNAME_MAX_SIZE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToOne(mappedBy = "user")
    private FcmDeviceToken fcmDeviceToken;

    @Enumerated(EnumType.STRING)
    private OAuthProviderEnum provider;

    @Column(nullable = true, unique = true)
    private String providerId;

    @Size(min = 1, max = NICKNAME_MAX_SIZE)
    @Column(nullable = true, unique = true)
    private String nickname;

    private String profileImgUrl;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    @Convert(converter = YesNoConverter.class)
    private Boolean isBlocked = false;

    private LocalDateTime leaveDate;

    public User() {
    }

    @Builder
    public User(Long id, OAuthProviderEnum provider, String providerId, String nickname, String profileImgUrl) {
        this.id = id;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    public void withdrawInformation() {
        this.nickname = null;
        this.providerId = null;
        this.provider = null;
        this.profileImgUrl = null;
        this.leaveDate = LocalDateTime.now();
    }

    public void block() {
        this.isBlocked = true;
    }
}
