package com.nexters.phochak.user.presentation;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.CommonResponse;
import com.nexters.phochak.user.NicknameModifyRequestDto;
import com.nexters.phochak.user.UserCheckResponseDto;
import com.nexters.phochak.user.UserInfoResponseDto;
import com.nexters.phochak.user.application.UserService;
import com.nexters.phochak.user.application.application.port.in.AuthUseCase;
import com.nexters.phochak.user.application.application.port.in.JwtResponseDto;
import com.nexters.phochak.user.application.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.application.port.in.LoginRequestDto;
import com.nexters.phochak.user.application.application.port.in.LogoutRequestDto;
import com.nexters.phochak.user.application.application.port.in.ReissueTokenRequestDto;
import com.nexters.phochak.user.application.application.port.in.WithdrawRequestDto;
import jakarta.validation.Valid;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/user")
@RestController
public class UserController {
    private final AuthUseCase authUserCase;
    private final UserService userService;
    private final JwtTokenUseCase jwtTokenUseCase;

    @GetMapping("/login/{provider}")
    public CommonResponse<JwtResponseDto> login(@PathVariable String provider, @Valid LoginRequestDto requestDto) {
        Long loginUserId = authUserCase.login(provider, requestDto);
        return new CommonResponse<>(jwtTokenUseCase.issueToken( loginUserId));
    }

    @PostMapping("/reissue-token")
    public CommonResponse<JwtResponseDto> reissue(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) {
        return new CommonResponse<>(jwtTokenUseCase.reissueToken(reissueTokenRequestDto));
    }

    @Auth
    @PostMapping("/logout")
    public CommonResponse<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        jwtTokenUseCase.logout(logoutRequestDto.getRefreshToken());
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

    @Auth
    @PostMapping("/withdraw")
    public CommonResponse<Void> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto) {
        Long userId = UserContext.CONTEXT.get();
        jwtTokenUseCase.logout(withdrawRequestDto.getRefreshToken());
        userService.withdraw(userId);
        return new CommonResponse<>();
    }

}
