package com.nexters.phochak.dto;

import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.Getter;

@Getter
public abstract class OAuthUserInformation {

    private OAuthProviderEnum provider;
    private String providerId;
    private String initialProfileImage;
}
