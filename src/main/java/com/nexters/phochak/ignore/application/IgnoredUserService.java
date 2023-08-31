package com.nexters.phochak.ignore.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserEntity;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserEntityRelation;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.ignore.application.port.in.IgnoredUserResponseDto;
import com.nexters.phochak.ignore.application.port.out.IgnoredUserUseCase;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IgnoredUserService implements IgnoredUserUseCase {
    private final IgnoredUserRepository ignoredUserRepository;
    private final UserRepository userRepository;
    @Override
    public void ignoreUser(Long me, Long ignoredUserId) {
        UserEntity userEntity = userRepository.getReferenceById(me);
        UserEntity pageOwner = userRepository.getBy(ignoredUserId);
        try {
            IgnoredUserEntityRelation ignoredUsersRelation = IgnoredUserEntityRelation.builder()
                    .user(userEntity)
                    .ignoredUser(pageOwner)
                    .build();
            IgnoredUserEntity ignoredUsers = IgnoredUserEntity.builder()
                    .ignoredUsersRelation(ignoredUsersRelation)
                    .build();
            ignoredUserRepository.save(ignoredUsers);
        } catch (
                DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_IGNORED_USER);
        }
    }

    @Override
    @Transactional
    public void cancelIgnoreUser(Long me, Long ignoredUserId) {
        ignoredUserRepository.deleteIgnore(me, ignoredUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IgnoredUserResponseDto> getIgnoreUserList(Long me) {
        List<IgnoredUserEntity> ignoreUserListByUserId = ignoredUserRepository.getIgnoreUserListByUserId(me);
        return IgnoredUserResponseDto.of(ignoreUserListByUserId);
    }
}
