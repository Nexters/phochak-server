package com.nexters.phochak.ignore.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface CancelIgnorePort {
    void cancelIgnore(User me, User pageOwner);
}
