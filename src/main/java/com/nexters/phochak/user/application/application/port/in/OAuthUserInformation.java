package com.nexters.phochak.user.application.application.port.in;

import com.nexters.phochak.user.domain.OAuthProviderEnum;
import lombok.Getter;

@Getter
public abstract class OAuthUserInformation {

    private OAuthProviderEnum provider;
    private String providerId;
    private String initialProfileImage;
}
