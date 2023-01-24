package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.response.LoginResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.service.UserService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String NICKNAME_PREFIX = "여행자 ";
    private static final String TOKEN_TYPE = "Bearer";
    private final Map<OAuthProviderEnum, OAuthService> oAuthServiceMap;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponseDto login(String provider, String code) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.valueOf(provider.toUpperCase());
        OAuthService oAuthService = oAuthServiceMap.get(providerEnum);

        OAuthUserInformation userInformation = oAuthService.requestUserInformation(code);
        Optional<User> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());

        User user = null;
        if (target.isPresent()) {
            user = target.orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);

        } else {
            log.info("UserServiceImpl|login(신규 회원): {}", userInformation);
            String nickname = generateInitialNickname(userInformation);

            User newUser = User.builder()
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .nickname(nickname)
                    .profileImgUrl(userInformation.getInitialProfileImage())
                    .build();

            user = userRepository.save(newUser);
        }
        return createLoginResponse(user.getId());
    }

    private static String generateInitialNickname(OAuthUserInformation userInformation) {
        // TODO: 초기 닉네임 설정 시 닉네임이 유일해야 하기 때문에 뒤에 난수나 고유한 채번로직이 들어가야 하는데 클라이언트와 협의 필요
        if (StringUtils.hasText(userInformation.getInitialNickname())) {
            return NICKNAME_PREFIX + userInformation.getInitialNickname() + userInformation.getProviderId().substring(0, 6);
        }
        return NICKNAME_PREFIX + userInformation.getProviderId().substring(0, 6);
    }

    private LoginResponseDto createLoginResponse(Long userId) {
        if (Objects.isNull(userId)) {
            throw new PhochakException(ResCode.NOT_FOUND_USER);
        }
        TokenDto accessToken = jwtTokenService.generateAccessToken(userId);

        return LoginResponseDto.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken.getTokenString())
                .expiresIn(accessToken.getExpiresIn())
                .build();
    }
}
