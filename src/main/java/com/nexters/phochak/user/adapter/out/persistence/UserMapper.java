package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(final UserEntity userEntity) {
        return User.toDomain(userEntity);
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
