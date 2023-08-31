package com.nexters.phochak.ignore.application.port.out;

import com.nexters.phochak.ignore.application.port.in.IgnoredUserResponseDto;

import java.util.List;

public interface IgnoredUserUseCase {
    void ignoreUser(Long me, Long ignoredUserId);

    void cancelIgnoreUser(Long me, Long ignoredUserId);

    List<IgnoredUserResponseDto> getIgnoreUserList(Long me);
}
