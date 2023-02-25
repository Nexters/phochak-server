package com.nexters.phochak.service.impl;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.OAuthUserInformation;
import com.nexters.phochak.dto.response.UserCheckResponseDto;
import com.nexters.phochak.dto.response.UserInfoResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.OAuthService;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.service.UserService;
import com.nexters.phochak.specification.OAuthProviderEnum;
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
public class UserServiceImpl implements UserService {
    private static final String NICKNAME_PREFIX = "여행자#";
    private final Map<OAuthProviderEnum, OAuthService> oAuthServiceMap;
    private final UserRepository userRepository;
    private final PostService postService;

    @Override
    public Long login(String provider, String code) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        OAuthService oAuthService = oAuthServiceMap.get(providerEnum);

        OAuthUserInformation userInformation = oAuthService.requestUserInformation(code);

        User user = getOrCreateUser(userInformation);

        return user.getId();
    }

    @Override
    public void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new PhochakException(ResCode.NOT_FOUND_USER);
        }
    }

    @Override
    public UserCheckResponseDto checkNicknameIsDuplicated(String nickname) {
        return UserCheckResponseDto.of(isDuplicatedNickname(nickname));
    }


    @Override
    public void modifyNickname(String nickname) {
        Long userId = UserContext.CONTEXT.get();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));

        if (isDuplicatedNickname(nickname)) {
            throw new PhochakException(ResCode.DUPLICATED_NICKNAME);
        }

        user.modifyNickname(nickname);
    }

    @Override
    public UserInfoResponseDto getInfo(Long pageOwnerId, Long userId) {
        User pageOwner;
        if (pageOwnerId == null) {
            pageOwner = userRepository.findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        } else {
            pageOwner = userRepository.findById(pageOwnerId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        }
        return UserInfoResponseDto.of(pageOwner, pageOwner.getId().equals(userId));
    }

    @Override
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        user.withdrawInformation();
//        postService.deleteAllPostByUser(user);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private User getOrCreateUser(OAuthUserInformation userInformation) {
        User user = null;
        Optional<User> target = userRepository.findByProviderAndProviderId(userInformation.getProvider(), userInformation.getProviderId());

        if (target.isPresent()) {
            user = target.orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
            log.info("UserServiceImpl|login(기존 회원): {}", userInformation);

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
