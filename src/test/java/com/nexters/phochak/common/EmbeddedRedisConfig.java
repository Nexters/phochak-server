package com.nexters.phochak.common;

import com.nexters.phochak.common.config.property.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Profile("test")
@Configuration
public class EmbeddedRedisConfig {

    private final int redisPort;
    private final String redisPassword;
    private RedisServer redisServer;

    public EmbeddedRedisConfig(RedisProperties redisProperties) {
        this.redisPort = redisProperties.getPort();
        this.redisPassword = redisProperties.getPassword();
    }

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning()? findAvailablePort() : redisPort;
        redisServer = RedisServer.builder().port(port).setting("requirepass " + redisPassword).build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 기본 포트가 사용중일 경우 사용중이지 않은 다른 포트 검색
     */
    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }
        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !StringUtils.isEmpty(pidInfo.toString());
    }
}
