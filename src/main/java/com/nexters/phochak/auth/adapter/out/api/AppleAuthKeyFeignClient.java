package com.nexters.phochak.auth.adapter.out.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleAuthKeyFeignClient", url = "${apple.auth-uri}")
public interface AppleAuthKeyFeignClient {

    @GetMapping
    String call();
}
