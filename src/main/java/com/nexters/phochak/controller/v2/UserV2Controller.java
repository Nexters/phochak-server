package com.nexters.phochak.controller.v2;

import com.nexters.phochak.dto.request.LoginV2RequestDto;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.JwtResponseDto;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v2/user")
@RestController
public class UserV2Controller {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/login/{provider}")
    public CommonResponse<JwtResponseDto> login(@PathVariable String provider, @Valid LoginV2RequestDto requestDto) {
        Long loginUserId = userService.login(provider, requestDto.getToken(), requestDto.getToken());
        return new CommonResponse<>(jwtTokenService.issueToken(loginUserId));
    }
}
