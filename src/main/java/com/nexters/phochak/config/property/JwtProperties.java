package com.nexters.phochak.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt.token")
public class JwtProperties {
    private final String secretKey;
    private final long accessTokenExpireLength;
    private final long refreshTokenExpireLength;
}
