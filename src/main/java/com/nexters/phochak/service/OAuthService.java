package com.nexters.phochak.service;

import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.specification.OAuthProviderEnum;

public interface OAuthService {
    OAuthProviderEnum getOAuthProvider();

    OAuthUserInformation requestUserInformation(String authorizationCode);
}
