package com.nexters.phochak.post.adapter.out.web;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserMonolithicAdapter implements LoadUserNetworkClient {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User load(final Long userId) {
        final UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        return userMapper.toDomain(userEntity);
    }
}
