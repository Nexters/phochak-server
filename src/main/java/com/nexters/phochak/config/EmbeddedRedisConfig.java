package com.nexters.phochak.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Profile("test")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int testRedisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        System.out.println("testRedisPort = " + testRedisPort);
        redisServer = new RedisServer(testRedisPort);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}