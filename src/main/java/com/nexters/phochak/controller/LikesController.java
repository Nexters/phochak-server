package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.PhochakService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/post/{postId}/likes")
public class LikesController {

    private final PhochakService phochakService;

    @Auth
    @PostMapping
    public CommonResponse<Void> addPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        try {
            phochakService.addPhochak(userId, postId);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_PHOCHAKED);
        }
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
