package com.nexters.phochak.controller;

import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.LoginResponseDto;
import com.nexters.phochak.dto.request.LoginRequestDto;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("login/{provider}")
    public CommonResponse<LoginResponseDto> login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = userService.login(provider, requestDto.getToken());
        return new CommonResponse<>(jwtTokenService.createLoginResponse(loginUserId));
    }

    // test(web oauth) 용 api, provider를 kakao_test 로 명시
    @GetMapping("test/login/{provider}")
    public CommonResponse<LoginResponseDto> login(@PathVariable String provider, @RequestParam String code) {
        Long loginUserId = userService.login(provider, code);
        return new CommonResponse<>(jwtTokenService.createLoginResponse(loginUserId));
    }
}
