package com.nexters.phochak.controller;

import com.nexters.phochak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("login/{provider}")
    public void login(@PathVariable String provider, @RequestParam String code) {
        userService.login(provider, code);
    }
}
