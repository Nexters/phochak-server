package com.nexters.phochak.controller;

import com.nexters.phochak.dto.request.ReissueAccessTokenRequestDto;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.request.LoginRequestDto;
import com.nexters.phochak.dto.request.NicknameModifyRequestDto;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.LoginResponseDto;
import com.nexters.phochak.dto.request.LoginRequestDto;
import com.nexters.phochak.dto.response.ReissueAccessTokenResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("login/{provider}")
    public CommonResponse<LoginResponseDto> login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = userService.login(provider, requestDto.getToken());
        return new CommonResponse<>(jwtTokenService.createLoginResponse(loginUserId));
    }

    @PostMapping("reissue-token")
    public CommonResponse<ReissueAccessTokenResponseDto> login(@RequestBody ReissueAccessTokenRequestDto reissueAccessTokenRequestDto) {
        return new CommonResponse<>(jwtTokenService.reissueAccessToken(reissueAccessTokenRequestDto));
    }

    // test(web oauth) 용 api, provider를 kakao_test 로 명시
    @GetMapping("test/login/{provider}")
    public CommonResponse<LoginResponseDto> login(@PathVariable String provider, @RequestParam String code) {
        Long loginUserId = userService.login(provider, code);
        return new CommonResponse<>(jwtTokenService.createLoginResponse(loginUserId));
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
            // 이미 중복된 nickname을 가진 row가 있는 경우
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
}
