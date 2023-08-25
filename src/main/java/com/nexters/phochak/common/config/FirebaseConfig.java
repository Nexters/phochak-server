package com.nexters.phochak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nexters.phochak.common.config.property.FirebaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Profile("prod")
@Configuration
public class FirebaseConfig {

    private final String firebaseScope;
    private final String privateKeyLocation;

    public FirebaseConfig(FirebaseProperties firebaseProperties) {
        this.firebaseScope = firebaseProperties.getFirebaseScope();
        this.privateKeyLocation = firebaseProperties.getPrivateKeyLocation();
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new FileInputStream(privateKeyLocation))
                .createScoped((List.of(firebaseScope)));

        FirebaseOptions secondaryAppConfig = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        return FirebaseApp.initializeApp(secondaryAppConfig);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
