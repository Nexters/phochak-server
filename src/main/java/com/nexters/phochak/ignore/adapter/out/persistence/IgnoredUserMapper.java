package com.nexters.phochak.ignore.adapter.out.persistence;

import com.nexters.phochak.ignore.domain.IgnoredUser;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IgnoredUserMapper {
    private final UserMapper userMapper;
    public IgnoredUser toDomain(final IgnoredUserEntity entity) {
        return IgnoredUser.toDomain(entity);
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
