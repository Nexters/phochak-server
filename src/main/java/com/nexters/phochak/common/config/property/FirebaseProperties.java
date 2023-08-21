package com.nexters.phochak.common.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private final String firebaseScope;
    private final String privateKeyLocation;
}
