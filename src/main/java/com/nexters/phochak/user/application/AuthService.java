package com.nexters.phochak.user.application;

import com.nexters.phochak.user.application.port.in.AuthUseCase;
import com.nexters.phochak.user.application.port.in.LoginRequestDto;
import com.nexters.phochak.user.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.application.port.out.CreateUserPort;
import com.nexters.phochak.user.application.port.out.NotificationTokenRegisterPort;
import com.nexters.phochak.user.application.port.out.OAuthRequestPort;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final Map<OAuthProviderEnum, OAuthRequestPort> oAuthRequestPortMap;
    private final CreateUserPort createUserPort;
    private final NotificationTokenRegisterPort notificationTokenRegisterPort;

    @Override
    public Long login(String provider, LoginRequestDto requestDto) {
        OAuthRequestPort oAuthRequestPort = getProperProviderPort(provider);
        OAuthUserInformation userInformation = oAuthRequestPort.requestUserInformation(requestDto.token());
        User user = createUserPort.getOrCreateUser(userInformation);
        if (requestDto.fcmDeviceToken() != null) {
            notificationTokenRegisterPort.register(user, requestDto.fcmDeviceToken(), requestDto.operatingSystem());
        }
        return user.getId();
    }

    private OAuthRequestPort getProperProviderPort(final String provider) {
        OAuthProviderEnum providerEnum = OAuthProviderEnum.codeOf(provider);
        return oAuthRequestPortMap.get(providerEnum);
    }
}
