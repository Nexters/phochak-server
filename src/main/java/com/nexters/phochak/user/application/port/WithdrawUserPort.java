package com.nexters.phochak.user.application.port;

import com.nexters.phochak.user.domain.User;

public interface WithdrawUserPort {
    void withdraw(User user);
}
