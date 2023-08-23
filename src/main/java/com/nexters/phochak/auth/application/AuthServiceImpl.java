package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.OAuthUserInformation;
import com.nexters.phochak.auth.presentation.LoginRequestDto;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.notification.application.NotificationService;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import com.nexters.phochak.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Map<OAuthProviderEnum, OAuthService> oAuthServiceMap;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private static final String NICKNAME_PREFIX = "여행자#";

    @Override
    public Long login(String provider, LoginRequestDto requestDto) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        OAuthService oAuthService = oAuthServiceMap.get(providerEnum);
        OAuthUserInformation userInformation = oAuthService.requestUserInformation(requestDto.token());
        User user = getOrCreateUser(userInformation);
        if (requestDto.fcmDeviceToken() != null) {
            notificationService.registryFcmDeviceToken(user, requestDto.fcmDeviceToken());
        }
        return user.getId();
    }

    User getOrCreateUser(OAuthUserInformation userInformation) {
        User user = null;
        Optional<User> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());

        if (target.isPresent()) {
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);
            user = target.orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        } else {
            log.info("UserServiceImpl|login(신규 회원): {}", userInformation);
            String nickname = generateInitialNickname();

            User newUser = User.builder()
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .nickname(nickname)
                    .profileImgUrl(userInformation.getInitialProfileImage())
                    .build();

            user = userRepository.save(newUser);
        }
        return user;
    }

    private static String generateInitialNickname() {
        // 초기 닉네임 여행자#난수 6자로 결정
        return NICKNAME_PREFIX + generateUUID();
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, User.NICKNAME_MAX_SIZE - NICKNAME_PREFIX.length());
    }
}
