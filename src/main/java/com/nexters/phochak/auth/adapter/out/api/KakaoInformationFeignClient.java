package com.nexters.phochak.auth.adapter.out.api;

import com.nexters.phochak.auth.application.port.in.KakaoUserInformation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoInformationFeignClient", url = "${kakao.api_url.information}")
public interface KakaoInformationFeignClient {

    @GetMapping
    KakaoUserInformation call(
            @RequestHeader("Content-Type") String contentType,
            @RequestHeader("Authorization") String accessToken
    );
}
