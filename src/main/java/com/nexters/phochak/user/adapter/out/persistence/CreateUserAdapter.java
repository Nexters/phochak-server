package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.auth.application.port.in.OAuthUserInformation;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.port.out.CreateUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateUserAdapter implements CreateUserPort {
    private final UserRepository userRepository;
    private static final String NICKNAME_PREFIX = "여행자#";

    @Override
    public User getOrCreateUser(OAuthUserInformation userInformation) {
        UserEntity userEntity = null;
        Optional<UserEntity> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());

        if (target.isPresent()) {
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);
            userEntity = target.orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
            return
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
        return user;
    }

    private static String generateInitialNickname() {
        // 초기 닉네임 여행자#난수 6자로 결정
        return NICKNAME_PREFIX + generateUUID();
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, UserEntity.NICKNAME_MAX_SIZE - NICKNAME_PREFIX.length());
    }
}
