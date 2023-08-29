package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.application.application.port.in.OAuthUserInformation;
import com.nexters.phochak.user.domain.User;

public interface CreateUserPort {
    User getOrCreateUser(OAuthUserInformation userInformation);
}
