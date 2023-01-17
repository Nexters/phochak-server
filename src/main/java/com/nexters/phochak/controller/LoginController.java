package com.nexters.phochak.controller;

import com.nexters.phochak.dto.JoinRequestDto;
import com.nexters.phochak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class LoginController {
    private final UserService userService;

    @GetMapping("login/{provider}")
    public void login(@PathVariable String provider, @RequestParam String code) {
        userService.login(provider, code);
    }

    @PostMapping("join/{provider}")
    public void join(@PathVariable String provider, @RequestBody JoinRequestDto joinRequestDto) {
        userService.join(provider, joinRequestDto);
    }
}
