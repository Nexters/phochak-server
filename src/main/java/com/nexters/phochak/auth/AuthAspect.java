package com.nexters.phochak.auth;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.JwtTokenService;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
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
    private final JwtTokenUseCase jwtTokenUseCase;
    private final HttpServletRequest httpServletRequest;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String WHITE_SPACE = " ";

    @Around("@annotation(com.nexters.phochak.auth.Auth)")
    public Object validateAccessToken(final ProceedingJoinPoint joinPoint) throws Throwable {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(accessToken)) {
            throw new PhochakException(ResCode.TOKEN_NOT_FOUND);
        }

        accessToken = JwtTokenService.parseOnlyTokenFromRequest(accessToken);
        Long userId = jwtTokenUseCase.validateJwt(accessToken);

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
