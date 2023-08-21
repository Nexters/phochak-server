package com.nexters.phochak.auth.presentation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleAuthKeyFeignClient", url = "${apple.auth-uri}")
public interface AppleAuthKeyFeignClient {

    @GetMapping
    String call();
}
