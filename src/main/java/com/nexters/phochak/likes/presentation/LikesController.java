package com.nexters.phochak.likes.presentation;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.likes.application.LikesService;
import com.nexters.phochak.post.application.port.in.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/post/{postId}/likes")
public class LikesController {

    private final LikesService likesService;

    @Auth
    @PostMapping
    public CommonResponseDto<Void> addPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        likesService.addPhochak(userId, postId);
        return new CommonResponseDto<>();
    }

    @Auth
    @DeleteMapping
    public CommonResponseDto<Void> cancelPhochak(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        likesService.cancelPhochak(userId, postId);
        return new CommonResponseDto<>();
    }
}
