package com.nexters.phochak.ignore.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.ignore.application.port.out.SaveIgnoreUserPort;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveIgnoredUserAdapter implements SaveIgnoreUserPort {
    private final UserMapper userMapper;
    private final IgnoredUserRepository ignoredUserRepository;

    @Override
    public void save(final User me, final User pageOwner) {
        final UserEntity meEntity = userMapper.toEntity(me);
        final UserEntity pageOwnerEntity = userMapper.toEntity(pageOwner);
        final IgnoredUserEntityRelation ignoredUsersRelation = new IgnoredUserEntityRelation(meEntity, pageOwnerEntity);
        final IgnoredUserEntity ignoredUsers = new IgnoredUserEntity(ignoredUsersRelation);
        try {
            ignoredUserRepository.save(ignoredUsers);
        } catch (DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_IGNORED_USER);
        }
    }
}
