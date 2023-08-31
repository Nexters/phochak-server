package com.nexters.phochak.user.application.port.out;

import com.nexters.phochak.user.domain.User;

public interface UpdateUserNicknamePort {
    boolean checkDuplicatedNickname(String nickname);

    void modifyNickname(final User user, String nickname);
}
