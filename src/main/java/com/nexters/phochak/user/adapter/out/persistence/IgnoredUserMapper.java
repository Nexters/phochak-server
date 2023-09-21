package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.domain.IgnoredUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IgnoredUserMapper {
    private final UserMapper userMapper;
    public IgnoredUser toDomain(final IgnoredUserEntity entity) {
        return new IgnoredUser(
                userMapper.toDomain(entity.getIgnoredUserRelation().getUser()),
                userMapper.toDomain(entity.getIgnoredUserRelation().getIgnoredUser())
        );
    }

    public IgnoredUserEntity toEntity(final IgnoredUser domain) {
        return new IgnoredUserEntity(
                new IgnoredUserEntityRelation(
                    userMapper.toEntity(domain.getUser()),
                    userMapper.toEntity(domain.getIgnoredUser())
                )
        );
    }
}
