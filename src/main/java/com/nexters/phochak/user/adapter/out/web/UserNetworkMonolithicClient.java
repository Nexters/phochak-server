package com.nexters.phochak.user.adapter.out.web;

import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNetworkMonolithicClient implements UserNetworkClient {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User loadUser(final Long userId) {
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDomain(userEntity);
    }
}
