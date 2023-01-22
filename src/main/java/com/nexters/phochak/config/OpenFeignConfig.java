package com.nexters.phochak.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.nexters.phochak.client")
public class OpenFeignConfig {
}
