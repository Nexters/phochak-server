package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface FindUserPort {
    User load(final Long userId);
}
