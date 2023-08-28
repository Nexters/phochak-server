package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.auth.application.port.in.OAuthUserInformation;
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
    private final UserMapper userMapper;
    private static final String NICKNAME_PREFIX = "여행자#";

    @Override
    public User getOrCreateUser(final OAuthUserInformation userInformation) {
        final Optional<UserEntity> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());
        if (target.isPresent()) {
            log.info("CreateUserAdapter|getOrCreateUser(기존 회원 로그인): {}", userInformation);
            return userMapper.toDomain(target.get());
        } else {
            log.info("CreateUserAdapter|getOrCreateUser(신규 회원 가입): {}", userInformation);
            final UserEntity userEntity = userRepository.save(new UserEntity(
                            userInformation.getProvider(),
                            userInformation.getProviderId(),
                            generateInitialNickname(),
                            userInformation.getInitialProfileImage()
                    ));
            return userMapper.toDomain(userEntity);
        }
    }

    private static String generateInitialNickname() {
        // 초기 닉네임 여행자#난수 6자로 결정
        return NICKNAME_PREFIX + generateUUID();
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, UserEntity.NICKNAME_MAX_SIZE - NICKNAME_PREFIX.length());
    }
}
