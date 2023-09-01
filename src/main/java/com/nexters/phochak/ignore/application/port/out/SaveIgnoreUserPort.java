package com.nexters.phochak.ignore.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface SaveIgnoreUserPort {
    void save(User me, User pageOwner);
}
