package com.nexters.phochak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleAuthKeyFeignClient", url = "https://appleid.apple.com/auth/keys")
public interface AppleAuthKeyFeignClient {

    @GetMapping
    String call();
}
