package com.nexters.phochak.dto;

import com.nexters.phochak.specification.OAuthProviderEnum;
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
