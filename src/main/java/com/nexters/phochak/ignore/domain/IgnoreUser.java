package com.nexters.phochak.ignore.domain;

import com.nexters.phochak.user.domain.User;
import lombok.Getter;

@Getter
public class IgnoreUser {
    private User user;
    private User ignoredUser;
}
