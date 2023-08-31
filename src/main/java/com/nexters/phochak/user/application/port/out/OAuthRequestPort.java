package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.domain.OAuthProviderEnum;

public interface OAuthRequestPort {
    OAuthProviderEnum getOAuthProvider();

    OAuthUserInformation requestUserInformation(String token);
}
