package com.nexters.phochak.ignore.presentation;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.ignore.IgnoredUserResponseDto;
import com.nexters.phochak.post.CommonResponse;
import com.nexters.phochak.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class IgnoreController {

    private final UserService userService;

    @Auth
    @GetMapping("/v1/user/ignore")
    public CommonResponse<List<IgnoredUserResponseDto>> getIgnoreUser() {
        Long me = UserContext.CONTEXT.get();
        return new CommonResponse<>(userService.getIgnoreUserList(me));
    }

    @Auth
    @PostMapping("/v1/user/ignore/{ignoredUserId}")
    public CommonResponse<Void> ignoreUser(@PathVariable(value = "ignoredUserId") Long ignoredUserId) {
        Long me = UserContext.CONTEXT.get();
        userService.ignoreUser(me, ignoredUserId);
        return new CommonResponse<>();
    }

    @Auth
    @DeleteMapping("/v1/user/ignore/{ignoredUserId}")
    public CommonResponse<Void> cancelIgnoreUser(@PathVariable(value = "ignoredUserId") Long ignoredUserId) {
        Long me = UserContext.CONTEXT.get();
        userService.cancelIgnoreUser(me, ignoredUserId);
        return new CommonResponse<>();
    }
}
