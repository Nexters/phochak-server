package com.nexters.phochak.ignore.application;

import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserEntity;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.ignore.application.port.in.IgnoredUserResponseDto;
import com.nexters.phochak.ignore.application.port.out.IgnoredUserUseCase;
import com.nexters.phochak.ignore.application.port.out.LoadIgnoredUserPort;
import com.nexters.phochak.ignore.application.port.out.SaveIgnoreUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IgnoredUserService implements IgnoredUserUseCase {
    private final LoadIgnoredUserPort loadIgnoredUserPort;
    private final IgnoredUserRepository ignoredUserRepository;
    private final SaveIgnoreUserPort saveIgnoredUserPort;

    @Override
    public void ignoreUser(Long myId, Long ignoredUserId) {
        User me = loadIgnoredUserPort.loadIgnoredUser(myId);
        User pageOwner = loadIgnoredUserPort.loadIgnoredUser(ignoredUserId);
        saveIgnoredUserPort.save(me, pageOwner);
    }

    @Override
    @Transactional
    public void cancelIgnoreUser(Long me, Long ignoredUserId) {
        ignoredUserRepository.deleteIgnore(me, ignoredUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IgnoredUserResponseDto> getIgnoreUserList(Long me) {
        List<IgnoredUserEntity> ignoreUserListByUserId = ignoredUserRepository.getIgnoreUserListByUserId(me);
        return IgnoredUserResponseDto.of(ignoreUserListByUserId);
    }
}
