package com.nexters.phochak.client;

import com.nexters.phochak.dto.KakaoAccessTokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoFeignClient", url = "${kakao.api_url.token}")
public interface KakaoAccessTokenFeignClient {

    @PostMapping
    KakaoAccessTokenResponseDto call(
            @RequestHeader("Content-Type") String contentType,
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );
}
