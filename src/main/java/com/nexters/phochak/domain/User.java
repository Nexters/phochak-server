package com.nexters.phochak.domain;

import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Entity
@Table(name = "`USER`")
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OAuthProviderEnum provider;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Size(min = 1, max = 20)
    @Column(nullable = false, unique = true)
    private String nickname;

    private String profileImgUrl;

    public User() {
    }

    @Builder
    public User(OAuthProviderEnum provider, String providerId, String nickname, String profileImgUrl) {
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
    }
}
