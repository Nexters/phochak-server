package com.nexters.phochak.auth.application.port.in;

import com.nexters.phochak.user.domain.OAuthProviderEnum;

public interface OAuthUseCase {
    OAuthProviderEnum getOAuthProvider();

    OAuthUserInformation requestUserInformation(String token);
}
