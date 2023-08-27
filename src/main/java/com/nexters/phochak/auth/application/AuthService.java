package com.nexters.phochak.auth.application;

import com.nexters.phochak.auth.application.port.in.AuthUseCase;
import com.nexters.phochak.auth.application.port.in.LoginRequestDto;
import com.nexters.phochak.auth.application.port.in.OAuthUserInformation;
import com.nexters.phochak.auth.application.port.out.OAuthRequestPort;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.notification.application.NotificationService;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
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
public class AuthService implements AuthUseCase {

    private final Map<OAuthProviderEnum, OAuthRequestPort> oAuthRequestPortMap;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
//    private final CreateUserPort createUserPort;

    private static final String NICKNAME_PREFIX = "여행자#";

    @Override
    public Long login(String provider, LoginRequestDto requestDto) {
        OAuthRequestPort oAuthRequestPort = getProperProviderPort(provider);
        OAuthUserInformation userInformation = oAuthRequestPort.requestUserInformation(requestDto.token());
//        User user = createUserPort.getOrCreateUser(userInformation);
        UserEntity userEntity = getOrCreateUser(userInformation);
        if (requestDto.fcmDeviceToken() != null) {
            notificationService.registryFcmDeviceToken(userEntity, requestDto.fcmDeviceToken());
        }
        return userEntity.getId();
    }

    UserEntity getOrCreateUser(OAuthUserInformation userInformation) {
        UserEntity userEntity = null;
        Optional<UserEntity> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());

        if (target.isPresent()) {
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);
            userEntity = target.orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        } else {
            log.info("UserServiceImpl|login(신규 회원): {}", userInformation);
            String nickname = generateInitialNickname();

            UserEntity newUserEntity = UserEntity.builder()
                    .provider(userInformation.getProvider())
                    .providerId(userInformation.getProviderId())
                    .nickname(nickname)
                    .profileImgUrl(userInformation.getInitialProfileImage())
                    .build();

            userEntity = userRepository.save(newUserEntity);
        }
        return userEntity;
    }

    private OAuthRequestPort getProperProviderPort(final String provider) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        OAuthRequestPort oAuthRequestPort = oAuthRequestPortMap.get(providerEnum);
        return oAuthRequestPort;
    }

    private static String generateInitialNickname() {
        // 초기 닉네임 여행자#난수 6자로 결정
        return NICKNAME_PREFIX + generateUUID();
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, UserEntity.NICKNAME_MAX_SIZE - NICKNAME_PREFIX.length());
    }
}
