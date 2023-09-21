package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.out.UpdateUserNicknamePort;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserNicknameAdapter implements UpdateUserNicknamePort {

    private final UserRepository userRepository;

    public UpdateUserNicknameAdapter(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean checkDuplicatedNickname(final String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}
