package com.nexters.phochak.ignore.application.port.in;

import com.nexters.phochak.ignore.domain.IgnoredUser;
import com.nexters.phochak.user.domain.User;

import java.util.List;

public interface LoadIgnoredListPort {
    List<IgnoredUser> loadIgnoredUserList(User me);
}
