package com.nexters.phochak.ignore.application;

import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.ignore.application.port.in.IgnoredUserResponseDto;
import com.nexters.phochak.ignore.application.port.in.LoadIgnoredListPort;
import com.nexters.phochak.ignore.application.port.out.CancelIgnorePort;
import com.nexters.phochak.ignore.application.port.out.IgnoredUserUseCase;
import com.nexters.phochak.ignore.application.port.out.LoadUserForIgnorePort;
import com.nexters.phochak.ignore.application.port.out.SaveIgnoreUserPort;
import com.nexters.phochak.ignore.domain.IgnoredUser;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IgnoredUserService implements IgnoredUserUseCase {
    private final LoadUserForIgnorePort loadUserForIgnorePort;
    private final IgnoredUserRepository ignoredUserRepository;
    private final SaveIgnoreUserPort saveIgnoredUserPort;
    private final CancelIgnorePort cancelIgnorePort;
    private final LoadIgnoredListPort loadIgnoredListPort;

    @Override
    public void ignoreUser(Long myId, Long ignoredUserId) {
        User me = loadUserForIgnorePort.loadUser(myId);
        User pageOwner = loadUserForIgnorePort.loadUser(ignoredUserId);
        saveIgnoredUserPort.save(me, pageOwner);
    }

    @Override
    @Transactional
    public void cancelIgnoreUser(Long myId, Long ignoredUserId) {
        User me = loadUserForIgnorePort.loadUser(myId);
        User pageOwner = loadUserForIgnorePort.loadUser(ignoredUserId);
        cancelIgnorePort.cancelIgnore(me, pageOwner);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IgnoredUserResponseDto> getIgnoreUserList(Long myId) {
        User me = loadUserForIgnorePort.loadUser(myId);
        List<IgnoredUser> ignoredUserList = loadIgnoredListPort.loadIgnoredUserList(me);
        return ignoredUserList.stream().map(IgnoredUserResponseDto::of).toList();
    }
}
