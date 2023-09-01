package com.nexters.phochak.user.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.post.application.port.in.PostUseCase;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.port.in.LoginRequestDto;
import com.nexters.phochak.user.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.application.port.in.UserCheckResponseDto;
import com.nexters.phochak.user.application.port.in.UserInfoResponseDto;
import com.nexters.phochak.user.application.port.in.UserUseCase;
import com.nexters.phochak.user.application.port.out.CreateUserPort;
import com.nexters.phochak.user.application.port.out.FindIgnoredUserPort;
import com.nexters.phochak.user.application.port.out.FindUserPort;
import com.nexters.phochak.user.application.port.out.NotificationTokenRegisterPort;
import com.nexters.phochak.user.application.port.out.OAuthRequestPort;
import com.nexters.phochak.user.application.port.out.UpdateUserNicknamePort;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final FindUserPort findUserPort;
    private final CreateUserPort createUserPort;
    private final FindIgnoredUserPort findIgnoredUserPort;
    private final UpdateUserNicknamePort updateUserNicknamePort;
    private final UserRepository userRepository;
    private final PostUseCase postUseCase;
    private final Map<OAuthProviderEnum, OAuthRequestPort> oAuthRequestPortMap;
    private final IgnoredUserRepository ignoredUserRepository;
    private final NotificationTokenRegisterPort notificationTokenRegisterPort;

    @Override
    public Long login(final String provider, final LoginRequestDto requestDto) {
        OAuthRequestPort oAuthRequestPort = getProperProviderPort(provider);
        OAuthUserInformation userInformation = oAuthRequestPort.requestUserInformation(requestDto.token());
        User user = createUserPort.getOrCreateUser(userInformation);
        if (requestDto.fcmDeviceToken() != null) {
            notificationTokenRegisterPort.register(user, requestDto.fcmDeviceToken(), requestDto.operatingSystem());
        }
        return user.getId();
    }

    @Override
    public UserCheckResponseDto checkNicknameIsDuplicated(final String nickname) {
        return new UserCheckResponseDto(updateUserNicknamePort.checkDuplicatedNickname(nickname));
    }


    @Override
    @Transactional
    public void modifyNickname(final Long userId, final String nickname) {
        if (updateUserNicknamePort.checkDuplicatedNickname(nickname)) {
            throw new PhochakException(ResCode.DUPLICATED_NICKNAME);
        }
        User user = findUserPort.load(userId);
        updateUserNicknamePort.modifyNickname(user, nickname);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto getInfo(final Long userId, final Long pageOwnerId) {
        final User user = findUserPort.load(userId);
        final User pageOwner = findUserPort.load(pageOwnerId);
        final boolean isIgnored = findIgnoredUserPort.checkIgnoredRelation(user, pageOwner);
        return UserInfoResponseDto.of(pageOwner, pageOwner.getId().equals(userId), isIgnored);
    }

    @Override
    public void withdraw(Long userId) {
        UserEntity userEntity = userRepository.getBy(userId);
        userEntity.withdrawInformation();
        postUseCase.deleteAllPostByUser(userEntity);
    }

    private OAuthRequestPort getProperProviderPort(final String provider) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        return oAuthRequestPortMap.get(providerEnum);
    }

}
