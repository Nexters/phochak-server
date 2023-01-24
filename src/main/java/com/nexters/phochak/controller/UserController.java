package com.nexters.phochak.controller;

import com.nexters.phochak.dto.response.LoginResponseDto;
import com.nexters.phochak.dto.LoginRequestDto;
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
@RequestMapping("/v1/user")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("login/{provider}")
    public LoginResponseDto login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        return userService.login(provider, requestDto.getCode());
    }
}
