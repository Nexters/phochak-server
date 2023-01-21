package com.nexters.phochak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KakaoUserInformation extends OAuthUserInformation {
    private static final OAuthProviderEnum provider = OAuthProviderEnum.KAKAO;

    @JsonProperty(value = "id")
    private String providerId;
    @JsonProperty(value = "connected_at")
    private String connectedAt;
    private KakaoOAuthProperties properties;

    @Override
    public String getInitialNickname() {
        return properties.getNickname();
    }

    @Override
    public String getInitialProfileImage() {
        return properties.thumbnailImage;
    }

    @Override
    public OAuthProviderEnum getProvider() {
        return provider;
    }

    @ToString
    @Getter
    static class KakaoOAuthProperties {
        private String nickname;
        @JsonProperty(value = "profile_image")
        private String profileImage;
        @JsonProperty(value = "thumbnail_image")
        private String thumbnailImage;
        // TODO: 아래 정보는 중복으로 받고있는데, 없앨 수 있는 방법 있는지 살펴봐야함
        @JsonProperty(value = "kakao_account")
        private String kakaoAccount;
    }
}

