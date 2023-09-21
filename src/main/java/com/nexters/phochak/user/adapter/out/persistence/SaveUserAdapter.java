package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.out.SaveUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveUserAdapter implements SaveUserPort {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public void save(final User user) {
        final UserEntity userEntity = userMapper.toEntity(user);
        userRepository.save(userEntity);
    }
}
