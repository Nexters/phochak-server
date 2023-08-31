package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.port.out.FindUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserAdapter implements FindUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User load(final Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        return userMapper.toDomain(userEntity);
    }
}
