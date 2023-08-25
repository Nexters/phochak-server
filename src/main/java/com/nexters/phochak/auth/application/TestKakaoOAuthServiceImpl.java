package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.OAuthUserInformation;
import com.nexters.phochak.auth.presentation.KakaoAccessTokenFeignClient;
import com.nexters.phochak.auth.presentation.KakaoAccessTokenResponseDto;
import com.nexters.phochak.auth.presentation.KakaoInformationFeignClient;
import com.nexters.phochak.common.config.property.KakaoLoginProperties;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
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
