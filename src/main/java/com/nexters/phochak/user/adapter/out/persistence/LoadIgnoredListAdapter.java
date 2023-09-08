package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.in.LoadIgnoredListPort;
import com.nexters.phochak.user.domain.IgnoredUser;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadIgnoredListAdapter implements LoadIgnoredListPort {
    private final UserMapper userMapper;
    private final IgnoredUserMapper ignoredUserMapper;
    private final IgnoredUserRepository ignoredUserRepository;

    @Override
    public List<IgnoredUser> loadIgnoredUserList(final User me) {
        final UserEntity entity = userMapper.toEntity(me);
        final List<IgnoredUserEntity> ignoredUserEntityList = ignoredUserRepository.getIgnoreUserListByUserId(entity);
        return ignoredUserEntityList.stream()
                .map(ignoredUserMapper::toDomain)
                .toList();
    }
}
