package com.nexters.phochak.config;

import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OAuthConfig {

    @Bean
    public Map<OAuthProviderEnum, OAuthService> oAuthServiceEnumMap(List<OAuthService> oAuthServices) {
        return new EnumMap<>(oAuthServices
                .stream()
                .collect(Collectors.toMap(OAuthService::getOAuthProvider, u -> u)));
    }
}
