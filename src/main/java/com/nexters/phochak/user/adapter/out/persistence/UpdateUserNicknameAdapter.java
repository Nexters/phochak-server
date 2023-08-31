package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.user.application.port.out.UpdateUserNicknamePort;
import com.nexters.phochak.user.domain.User;
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

    @Override
    public void modifyNickname(final User user, final String nickname) {
        final UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        userEntity.modifyNickname(nickname);
        user.updateNickname(nickname);
    }
}
