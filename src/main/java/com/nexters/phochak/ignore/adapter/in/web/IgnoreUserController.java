package com.nexters.phochak.ignore.adapter.in.web;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.ignore.application.port.in.IgnoredUserResponseDto;
import com.nexters.phochak.ignore.application.port.out.IgnoredUserUseCase;
import com.nexters.phochak.post.application.port.in.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class IgnoreUserController {

    private final IgnoredUserUseCase ignoredUserUseCase;

    @Auth
    @GetMapping("/v1/user/ignore")
    public CommonResponseDto<List<IgnoredUserResponseDto>> getIgnoreUser() {
        Long me = UserContext.CONTEXT.get();
        return new CommonResponseDto<>(ignoredUserUseCase.getIgnoreUserList(me));
    }

    @Auth
    @PostMapping("/v1/user/ignore/{ignoredUserId}")
    public CommonResponseDto<Void> ignoreUser(@PathVariable(value = "ignoredUserId") Long ignoredUserId) {
        Long me = UserContext.CONTEXT.get();
        ignoredUserUseCase.ignoreUser(me, ignoredUserId);
        return new CommonResponseDto<>();
    }

    @Auth
    @DeleteMapping("/v1/user/ignore/{ignoredUserId}")
    public CommonResponseDto<Void> cancelIgnoreUser(@PathVariable(value = "ignoredUserId") Long ignoredUserId) {
        Long me = UserContext.CONTEXT.get();
        ignoredUserUseCase.cancelIgnoreUser(me, ignoredUserId);
        return new CommonResponseDto<>();
    }
}
