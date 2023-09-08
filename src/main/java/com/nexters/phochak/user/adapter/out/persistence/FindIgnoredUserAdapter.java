package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.application.port.out.FindIgnoredUserPort;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FindIgnoredUserAdapter implements FindIgnoredUserPort {
    private final UserMapper userMapper;
    private final IgnoredUserRepository ignoredUserRepository;

    @Override
    public boolean checkIgnoredRelation(final User user, final User pageOwner) {
        if (Objects.equals(user.getId(), pageOwner.getId())) {
            return false;
        }
        final UserEntity userEntity = userMapper.toEntity(user);
        final UserEntity pageOwnerEntity = userMapper.toEntity(pageOwner);
        IgnoredUserEntityRelation ignoredUsersRelation = new IgnoredUserEntityRelation(userEntity, pageOwnerEntity);
        return ignoredUserRepository.existsByIgnoredUserRelation(ignoredUsersRelation);
    }
}
