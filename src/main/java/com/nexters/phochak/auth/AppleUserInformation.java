package com.nexters.phochak.auth;

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
public class AppleUserInformation extends OAuthUserInformation {
    private static final OAuthProviderEnum provider = OAuthProviderEnum.APPLE;
    private String providerId;

    @Override
    public OAuthProviderEnum getProvider() {
        return provider;
    }

}
