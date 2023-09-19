package com.nexters.phochak.user.adapter.out.persistent;

import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.port.WithdrawUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawUserAdapter implements WithdrawUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void withdraw(final User user) {
        userRepository.delete(userMapper.toEntity(user));
        user.withdraw();
    }
}
