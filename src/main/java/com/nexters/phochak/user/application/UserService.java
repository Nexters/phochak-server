package com.nexters.phochak.user.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.port.WithdrawUserPort;
import com.nexters.phochak.user.application.port.in.LoginRequestDto;
import com.nexters.phochak.user.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.application.port.in.UserCheckResponseDto;
import com.nexters.phochak.user.application.port.in.UserInfoResponseDto;
import com.nexters.phochak.user.application.port.in.UserUseCase;
import com.nexters.phochak.user.application.port.out.CreateUserPort;
import com.nexters.phochak.user.application.port.out.DeleteAllPostPort;
import com.nexters.phochak.user.application.port.out.FindIgnoredUserPort;
import com.nexters.phochak.user.application.port.out.FindUserPort;
import com.nexters.phochak.user.application.port.out.NotificationTokenRegisterPort;
import com.nexters.phochak.user.application.port.out.OAuthRequestPort;
import com.nexters.phochak.user.application.port.out.SaveUserPort;
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

    private final Map<OAuthProviderEnum, OAuthRequestPort> oAuthRequestPortMap;
    private final FindUserPort findUserPort;
    private final CreateUserPort createUserPort;
    private final FindIgnoredUserPort findIgnoredUserPort;
    private final UpdateUserNicknamePort updateUserNicknamePort;
    private final NotificationTokenRegisterPort notificationTokenRegisterPort;
    private final WithdrawUserPort withdrawUserPort;
    private final DeleteAllPostPort deleteAllPostPort;
    private final SaveUserPort saveUserPort;

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
    public void modifyNickname(final Long userId, final String nickname) {
        if (updateUserNicknamePort.checkDuplicatedNickname(nickname)) {
            throw new PhochakException(ResCode.DUPLICATED_NICKNAME);
        }
        User user = findUserPort.load(userId);
        user.modifyNickname(nickname);
        saveUserPort.save(user);
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
        final User user = findUserPort.load(userId);
        withdrawUserPort.withdraw(user);
        deleteAllPostPort.deleteAllPostByUser(user);
    }

    private OAuthRequestPort getProperProviderPort(final String provider) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        return oAuthRequestPortMap.get(providerEnum);
    }

}
