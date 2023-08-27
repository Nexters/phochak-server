package com.nexters.phochak.user.adapter.out.persistence;

import com.nexters.phochak.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity userEntity) {
        return User.toDomain(userEntity);
    }
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getFcmDeviceToken(),
                user.getProvider(),
                user.getProviderId(),
                user.getNickname(),
                user.getProfileImgUrl(),
                user.getIsBlocked(),
                user.getLeaveDate()
        );
    }
}
