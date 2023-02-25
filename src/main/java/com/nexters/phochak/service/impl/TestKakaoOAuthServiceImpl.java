package com.nexters.phochak.service.impl;

import com.nexters.phochak.client.KakaoAccessTokenFeignClient;
import com.nexters.phochak.client.KakaoInformationFeignClient;
import com.nexters.phochak.config.property.KakaoLoginProperties;
import com.nexters.phochak.dto.KakaoAccessTokenResponseDto;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestKakaoOAuthServiceImpl implements OAuthService {
    private static final OAuthProviderEnum OAUTH_PROVIDER = OAuthProviderEnum.KAKAO_TEST;
    private static final String TOKEN_PREFIX = "Bearer ";
    private final KakaoAccessTokenFeignClient kakaoAccessTokenFeignClient;
    private final KakaoInformationFeignClient kakaoInformationFeignClient;
    private final KakaoLoginProperties kakaoLoginProperties;

    @Override
    public OAuthProviderEnum getOAuthProvider() {
        return OAUTH_PROVIDER;
    }

    @Override
    public OAuthUserInformation requestUserInformation(String authorizationCode) {
        KakaoAccessTokenResponseDto result = kakaoAccessTokenFeignClient.call(
                "application/x-www-form-urlencoded",
                "authorization_code",
                kakaoLoginProperties.getClientId(),
                kakaoLoginProperties.getRedirectUri(),
                authorizationCode
        );

        return kakaoInformationFeignClient.call(
                "application/x-www-form-urlencoded;charset=utf-8",
                TOKEN_PREFIX + result.getAccessToken()
        );
    }
}
