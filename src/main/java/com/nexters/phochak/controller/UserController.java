package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.request.LoginRequestDto;
import com.nexters.phochak.dto.request.LogoutRequestDto;
import com.nexters.phochak.dto.request.NicknameModifyRequestDto;
import com.nexters.phochak.dto.request.ReissueTokenRequestDto;
import com.nexters.phochak.dto.request.WithdrawRequestDto;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.JwtResponseDto;
import com.nexters.phochak.dto.response.UserCheckResponseDto;
import com.nexters.phochak.dto.response.UserInfoResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/login/{provider}")
    public CommonResponse<JwtResponseDto> login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = userService.login(provider, requestDto.getToken());
        return new CommonResponse<>(jwtTokenService.issueToken( loginUserId));
    }

    // test(web oauth) ??? api, provider??? kakao_test ??? ??????
    @GetMapping("/test/login/{provider}")
    public CommonResponse<JwtResponseDto> login(@PathVariable String provider, @RequestParam String code) {
        Long loginUserId = userService.login(provider, code);
        return new CommonResponse<>(jwtTokenService.issueToken(loginUserId));
    }

    @PostMapping("/reissue-token")
    public CommonResponse<JwtResponseDto> reissue(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) {
        return new CommonResponse<>(jwtTokenService.reissueToken(reissueTokenRequestDto));
    }

    @Auth
    @PostMapping("/logout")
    public CommonResponse<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        jwtTokenService.logout(logoutRequestDto.getRefreshToken());
        return new CommonResponse<>();
    }

    @GetMapping("/check/nickname")
    public CommonResponse<UserCheckResponseDto> checkNicknameIsDuplicated(@RequestParam String nickname) {
        return new CommonResponse<>(userService.checkNicknameIsDuplicated(nickname));
    }

    @Auth
    @PutMapping("nickname")
    public CommonResponse<Void> modifyNickname(@RequestBody @Valid NicknameModifyRequestDto request) {
        try {
            userService.modifyNickname(request.getNickname());
        } catch (DataIntegrityViolationException e) {
            // ?????? ????????? nickname??? ?????? row??? ?????? ??????
            throw new PhochakException(ResCode.DUPLICATED_NICKNAME);
        }
        return new CommonResponse<>();
    }

    @Auth
    @GetMapping({"/{userId}", "/"})
    public CommonResponse<UserInfoResponseDto> getInfo(@PathVariable(value = "userId", required = false) Long pageOwnerId) {
        Long userId = UserContext.CONTEXT.get();
        return new CommonResponse<>(userService.getInfo(pageOwnerId, userId));
    }

    @Auth
    @PostMapping("/withdraw")
    public CommonResponse<Void> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        Long userId = UserContext.CONTEXT.get();
        jwtTokenService.logout(withdrawRequestDto.getRefreshToken());
        userService.withdraw(userId);
        return new CommonResponse<>();
    }
}
