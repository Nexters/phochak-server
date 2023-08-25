package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.OAuthUserInformation;
import com.nexters.phochak.user.domain.OAuthProviderEnum;

public interface OAuthService {
    OAuthProviderEnum getOAuthProvider();

    OAuthUserInformation requestUserInformation(String token);
}
