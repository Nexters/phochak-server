package com.nexters.phochak.user.adapter.in.web;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.post.application.port.in.CommonResponseDto;
import com.nexters.phochak.user.application.port.in.JwtResponseDto;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.port.in.LoginRequestDto;
import com.nexters.phochak.user.application.port.in.LogoutRequestDto;
import com.nexters.phochak.user.application.port.in.NicknameModifyRequestDto;
import com.nexters.phochak.user.application.port.in.ReissueTokenRequestDto;
import com.nexters.phochak.user.application.port.in.UserCheckResponseDto;
import com.nexters.phochak.user.application.port.in.UserInfoResponseDto;
import com.nexters.phochak.user.application.port.in.UserUseCase;
import com.nexters.phochak.user.application.port.in.WithdrawRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@RestController
public class UserController {
    private final UserUseCase userUseCase;
    private final JwtTokenUseCase jwtTokenUseCase;

    @GetMapping("/login/{provider}")
    public CommonResponseDto<JwtResponseDto> login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = userUseCase.login(provider, requestDto);
        return new CommonResponseDto<>(jwtTokenUseCase.issueToken( loginUserId));
    }

    @Auth
    @PostMapping("/logout")
    public CommonResponseDto<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        jwtTokenUseCase.logout(logoutRequestDto.getRefreshToken());
        return new CommonResponseDto<>();
    }

    @PostMapping("/reissue-token")
    public CommonResponseDto<JwtResponseDto> reissue(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) {
        return new CommonResponseDto<>(jwtTokenUseCase.reissueToken(reissueTokenRequestDto));
    }

    @Auth
    @PostMapping("/withdraw")
    public CommonResponseDto<Void> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        Long userId = UserContext.CONTEXT.get();
        jwtTokenUseCase.logout(withdrawRequestDto.getRefreshToken());
        userUseCase.withdraw(userId);
        return new CommonResponseDto<>();
    }

    @Auth
    @PutMapping("nickname")
    public CommonResponseDto<Void> modifyNickname(@RequestBody @Valid NicknameModifyRequestDto request) {
        Long userId = UserContext.CONTEXT.get();
        userUseCase.modifyNickname(userId, request.nickname());
        return new CommonResponseDto<>();
    }

    @GetMapping("/check/nickname")
    public CommonResponseDto<UserCheckResponseDto> checkNicknameIsDuplicated(@RequestParam String nickname) {
        return new CommonResponseDto<>(userUseCase.checkNicknameIsDuplicated(nickname));
    }


    @Auth
    @GetMapping({"/{userId}", "/"})
    public CommonResponseDto<UserInfoResponseDto> getInfo(@PathVariable(value = "userId", required = false) Long pageOwnerId) {
        Long userId = UserContext.CONTEXT.get();
        if (pageOwnerId == null) {
            pageOwnerId = userId;
        }
        return new CommonResponseDto<>(userUseCase.getInfo(userId, pageOwnerId));
    }


}
