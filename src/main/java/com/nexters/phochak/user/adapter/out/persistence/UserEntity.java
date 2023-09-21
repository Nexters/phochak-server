package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.common.domain.BaseTime;
import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
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
import lombok.Getter;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;

import static com.nexters.phochak.user.domain.User.NICKNAME_MAX_SIZE;

@Getter
@Entity
@Table(name = "USERS")
public class UserEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToOne(mappedBy = "user")
    private FcmDeviceTokenEntity fcmDeviceToken;

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

    public UserEntity() {
    }

    UserEntity(final Long id, final FcmDeviceTokenEntity fcmDeviceToken, final OAuthProviderEnum provider, final String providerId, final String nickname, final String profileImgUrl, final Boolean isBlocked, final LocalDateTime leaveDate) {
        this.id = id;
        this.fcmDeviceToken = fcmDeviceToken;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isBlocked = isBlocked;
        this.leaveDate = leaveDate;
    }

}
