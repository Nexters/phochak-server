package com.nexters.phochak.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private final String firebaseScope;
    private final String privateKeyLocation;
}
