package com.nexters.phochak.user.application.port.out;

public interface UpdateUserNicknamePort {
    boolean checkDuplicatedNickname(String nickname);
}
