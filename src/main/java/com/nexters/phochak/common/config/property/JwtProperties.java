package com.nexters.phochak.common.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {
    private final String secretKey;
    private final long accessTokenExpireLength;
    private final long refreshTokenExpireLength;
}
