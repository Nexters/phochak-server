package com.nexters.phochak.config;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.nexters.phochak.config.property.FirebaseProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class FirebaseConfig {

    private final String projectId;

    private final String privateKey;

    public FirebaseConfig(FirebaseProperties firebaseProperties) {
        this.projectId = firebaseProperties.getProjectId();
        this.privateKey = firebaseProperties.getPrivateKey();
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(
                        new ByteArrayInputStream(privateKey.getBytes())))
                .setProjectId(projectId)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
