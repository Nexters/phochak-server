package com.nexters.phochak.user.application.port.in;

import com.nexters.phochak.user.domain.IgnoredUser;
import com.nexters.phochak.user.domain.User;

import java.util.List;

public interface LoadIgnoredListPort {
    List<IgnoredUser> loadIgnoredUserList(User me);
}
