package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.out.CancelIgnorePort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelIgnoreAdapter implements CancelIgnorePort {
    private final UserMapper userMapper;
    private final IgnoredUserRepository ignoredUserRepository;

    @Override
    public void cancelIgnore(final User me, final User pageOwner) {
        final UserEntity meEntity = userMapper.toEntity(me);
        final UserEntity pageOwnerEntity = userMapper.toEntity(pageOwner);
        final IgnoredUserEntityRelation ignoredUsersRelation = new IgnoredUserEntityRelation(meEntity, pageOwnerEntity);
        ignoredUserRepository.deleteById(ignoredUsersRelation);
    }
}
