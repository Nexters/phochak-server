package com.nexters.phochak.common.config;

import com.nexters.phochak.auth.application.port.out.OAuthRequestPort;
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
    public Map<OAuthProviderEnum, OAuthRequestPort> oAuthServiceEnumMap(List<OAuthRequestPort> oAuthRequestPorts) {
        return new EnumMap<>(oAuthRequestPorts
                .stream()
                .collect(Collectors.toMap(OAuthRequestPort::getOAuthProvider, u -> u)));
    }
}
