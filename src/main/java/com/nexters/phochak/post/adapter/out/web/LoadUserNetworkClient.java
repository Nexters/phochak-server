package com.nexters.phochak.post.adapter.out.web;

import com.nexters.phochak.user.domain.User;

public interface LoadUserNetworkClient {
    User load(Long userId);
}
