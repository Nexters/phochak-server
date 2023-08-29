package com.nexters.phochak.auth;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.JwtTokenService;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.port.in.UserUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.nexters.phochak.user.application.port.in.JwtTokenUseCase.TokenVo.TOKEN_TYPE;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AuthAspect {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String WHITE_SPACE = " ";
    private final HttpServletRequest httpServletRequest;
    private final JwtTokenUseCase jwtTokenUseCase;
    private final UserUseCase userService;

    @Around("@annotation(com.nexters.phochak.auth.Auth)")
    public Object validateAccessToken(final ProceedingJoinPoint joinPoint) throws Throwable {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            throw new PhochakException(ResCode.TOKEN_NOT_FOUND);
        }

        accessToken = JwtTokenService.parseOnlyTokenFromRequest(accessToken);
        Long userId = jwtTokenUseCase.validateJwt(accessToken);

        // 유저 검증 후 Thread Local 변수에 할당
        try {
            userService.validateUser(userId);
        } catch (PhochakException e) {
            log.warn("AuthAspect|Invalid User: {}", userId, e);
            throw e;
        }
        UserContext.CONTEXT.set(userId);

        return joinPoint.proceed();
    }

    private static String parseToken(String accessToken) {
        if (!accessToken.startsWith(TOKEN_TYPE + WHITE_SPACE)) {
            throw new PhochakException(ResCode.INVALID_TOKEN);
        }
        return accessToken.substring(TOKEN_TYPE.length()).trim();
    }
}
