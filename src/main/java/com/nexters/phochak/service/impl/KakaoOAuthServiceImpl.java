package com.nexters.phochak.service.impl;

import com.nexters.phochak.client.KakaoAccessTokenFeignClient;
import com.nexters.phochak.client.KakaoInformationFeignClient;
import com.nexters.phochak.dto.KakaoAccessTokenResponseDto;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOAuthServiceImpl implements OAuthService {
    private static final OAuthProviderEnum OAUTH_PROVIDER = OAuthProviderEnum.KAKAO;
    private static final String TOKEN_PREFIX = "Bearer ";
    private final KakaoAccessTokenFeignClient kakaoAccessTokenFeignClient;
    private final KakaoInformationFeignClient kakaoInformationFeignClient;
    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Override
    public OAuthProviderEnum getOAuthProvider() {
        return OAUTH_PROVIDER;
    }

    @Override
    public OAuthUserInformation requestUserInformation(String authorizationCode) {
        KakaoAccessTokenResponseDto result = kakaoAccessTokenFeignClient.call(
                "application/x-www-form-urlencoded",
                "authorization_code",
                clientId,
                redirectUri,
                authorizationCode
        );

        return kakaoInformationFeignClient.call(
                "application/x-www-form-urlencoded;charset=utf-8",
                TOKEN_PREFIX + result.getAccessToken()
        );
    }
}
