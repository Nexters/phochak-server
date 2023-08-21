package com.nexters.phochak.auth.presentation;

import com.nexters.phochak.auth.JwtResponseDto;
import com.nexters.phochak.auth.application.JwtTokenService;
import com.nexters.phochak.post.CommonResponse;
import com.nexters.phochak.user.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v2/auth")
@RestController
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/login/{provider}")
    public CommonResponse<JwtResponseDto> loginV2(@PathVariable String provider, @Valid LoginV2RequestDto requestDto) {
        Long loginUserId = userService.login(provider, requestDto.getToken(), requestDto.getToken());
        return new CommonResponse<>(jwtTokenService.issueToken(loginUserId));
    }
}
