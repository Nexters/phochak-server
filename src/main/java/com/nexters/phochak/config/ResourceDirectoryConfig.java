package com.nexters.phochak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ResourceDirectoryConfig implements CommandLineRunner {

    @Value("${app.resource.local.shorts}")
    private String RESOURCE_PATH;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("RESOURCE_PATH = " + RESOURCE_PATH);
        try {
            Files.createDirectories(Paths.get(RESOURCE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
