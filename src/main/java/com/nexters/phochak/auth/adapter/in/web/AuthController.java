package com.nexters.phochak.auth.adapter.in.web;

import com.nexters.phochak.auth.application.port.in.AuthProcessUseCase;
import com.nexters.phochak.auth.application.port.in.JwtResponseDto;
import com.nexters.phochak.auth.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.auth.application.port.in.LoginRequestDto;
import com.nexters.phochak.post.CommonResponse;
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
    private final AuthProcessUseCase authService;
    private final JwtTokenUseCase jwtTokenUseCase;

    @GetMapping("/login/{provider}")
    public CommonResponse<JwtResponseDto> loginV2(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = authService.login(provider, requestDto);
        return new CommonResponse<>(jwtTokenUseCase.issueToken(loginUserId));
    }
}
