package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(final UserEntity userEntity) {
        final User user = new User(
                userEntity.getFcmDeviceToken(),
                userEntity.getProvider(),
                userEntity.getProviderId(),
                userEntity.getNickname(),
                userEntity.getProfileImgUrl(),
                userEntity.getIsBlocked(),
                userEntity.getLeaveDate()
        );
        user.assignId(userEntity.getId());
        return user;
    }

    public UserEntity toEntity(final User user) {
        return new UserEntity(
                user.getId(),
                user.getFcmDeviceTokenEntity(),
                user.getProvider(),
                user.getProviderId(),
                user.getNickname(),
                user.getProfileImgUrl(),
                user.getIsBlocked(),
                user.getLeaveDate()
        );
    }
}
