package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.service.UserService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String NICKNAME_PREFIX = "여행자 ";

    private final Map<OAuthProviderEnum, OAuthService> oAuthServiceMap;
    private final UserRepository userRepository;

    @Override
    public void login(String provider, String code) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.valueOf(provider.toUpperCase());
        OAuthService oAuthService = oAuthServiceMap.get(providerEnum);
        OAuthUserInformation userInformation = oAuthService.requestUserInformation(code);
        if (userRepository.existsByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId())) {
            // TODO: 로그인 처리
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);
        } else {
            log.info("UserServiceImpl|login(신규 회원): {}", userInformation);
            String nickname = generateInitialNickname(userInformation);

            User user = User.builder()
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .nickname(nickname)
                    .profileImgUrl(userInformation.getInitialProfileImage())
                    .build();

            userRepository.save(user);

            // TODO: 토큰 반환하여 로그인 처리
        }
    }

    private String generateInitialNickname(OAuthUserInformation userInformation) {
        // TODO: 초기 닉네임 설정 시 닉네임이 유일해야 하기 때문에 뒤에 난수나 고유한 채번로직이 들어가야 하는데 클라이언트와 협의 필요
        if (StringUtils.hasText(userInformation.getInitialNickname())) {
            return NICKNAME_PREFIX + userInformation.getInitialNickname() + userInformation.getProviderId().substring(0, 6);
        }
        return NICKNAME_PREFIX + userInformation.getProviderId().substring(0, 6);
    }
}
