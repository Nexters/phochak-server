package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.service.PhochakService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/post/{postId}/phochak")
public class PhochakController {

    private final PhochakService phochakService;

    @Auth
    @PostMapping
    public CommonResponse<Void> addPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        phochakService.addPhochak(userId, postId);
        return new CommonResponse<>();
    }

    @Auth
    @DeleteMapping
    public CommonResponse<Void> cancelPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        phochakService.cancelPhochak(userId, postId);
        return new CommonResponse<>();
    }
}
