package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.PhochakService;
import com.nexters.phochak.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/post/{postId}/phochak")
public class PhochakController {

    private final PhochakService phochakService;

    @Auth
    @PostMapping
    public CommonResponseDto<Void> addPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        phochakService.addPhochak(userId, postId);
        return new CommonResponseDto(ResCode.OK);
    }

    @Auth
    @DeleteMapping
    public CommonResponseDto<Void> cancelPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        phochakService.cancelPhochak(userId, postId);
        return new CommonResponseDto(ResCode.OK);
    }
}
