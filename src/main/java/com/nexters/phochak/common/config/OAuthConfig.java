package com.nexters.phochak.common.config;

import com.nexters.phochak.auth.application.port.in.OAuthUseCase;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OAuthConfig {

    @Bean
    public Map<OAuthProviderEnum, OAuthUseCase> oAuthServiceEnumMap(List<OAuthUseCase> oAuthUseCases) {
        return new EnumMap<>(oAuthUseCases
                .stream()
                .collect(Collectors.toMap(OAuthUseCase::getOAuthProvider, u -> u)));
    }
}
