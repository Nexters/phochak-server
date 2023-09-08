package com.nexters.phochak.user.adapter.out.web;

import com.nexters.phochak.user.domain.User;

public interface UserNetworkClient {
    User loadUser(Long myId);
}
