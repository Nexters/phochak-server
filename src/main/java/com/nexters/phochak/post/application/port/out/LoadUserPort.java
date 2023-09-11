package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface LoadUserPort {
    User load(Long userId);
}
