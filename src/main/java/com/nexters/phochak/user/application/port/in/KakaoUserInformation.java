package com.nexters.phochak.user.application.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    public String getInitialProfileImage() {
        return properties.thumbnailImage;
    }

    @Override
    public OAuthProviderEnum getProvider() {
        return provider;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Getter
    public static class KakaoOAuthProperties {
        private String nickname;
        @JsonProperty(value = "profile_image")
        private String profileImage;
        @JsonProperty(value = "thumbnail_image")
        private String thumbnailImage;
        // TODO: 아래 정보는 중복으로 받고있는데, 없앨 수 있는 방법 있는지 살펴봐야함
        @JsonProperty(value = "kakao_account")
        private String kakaoAccount;
    }

    public KakaoUserInformation(
            final String providerId,
            final String connectedAt,
            final String nickname,
            final String profileImage,
            final String thumbnailImage,
            final String kakaoAccount) {
        this.providerId = providerId;
        this.connectedAt = connectedAt;
        this.properties = new KakaoOAuthProperties(
                nickname,
                profileImage,
                thumbnailImage,
                kakaoAccount);
    }
}

