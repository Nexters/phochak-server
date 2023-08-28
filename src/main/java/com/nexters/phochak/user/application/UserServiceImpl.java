package com.nexters.phochak.user.application;

import com.nexters.phochak.auth.interceptor.UserContext;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.ignore.IgnoredUserResponseDto;
import com.nexters.phochak.ignore.domain.IgnoredUserRepository;
import com.nexters.phochak.ignore.domain.IgnoredUsers;
import com.nexters.phochak.ignore.domain.IgnoredUsersRelation;
import com.nexters.phochak.post.application.PostService;
import com.nexters.phochak.user.UserCheckResponseDto;
import com.nexters.phochak.user.UserInfoResponseDto;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IgnoredUserRepository ignoredUserRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    @Override
    public void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new PhochakException(ResCode.NOT_FOUND_USER);
        }
    }

    @Override
    public UserCheckResponseDto checkNicknameIsDuplicated(String nickname) {
        return UserCheckResponseDto.of(isDuplicatedNickname(nickname));
    }


    @Override
    public void modifyNickname(String nickname) {
        Long userId = UserContext.CONTEXT.get();
        UserEntity userEntity = userRepository.getBy(userId);

        if (isDuplicatedNickname(nickname)) {
            throw new PhochakException(ResCode.DUPLICATED_NICKNAME);
        }

        userEntity.modifyNickname(nickname);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto getInfo(Long pageOwnerId, Long userId) {
        UserEntity pageOwner;
        Boolean isIgnored = false;
        if (pageOwnerId == null) {
            pageOwner = userRepository.getBy(userId);
        } else {
            pageOwner = userRepository.getBy(pageOwnerId);
            UserEntity userEntity = userRepository.getReferenceById(userId);
            IgnoredUsersRelation ignoredUsersRelation = IgnoredUsersRelation.builder()
                    .user(userEntity)
                    .ignoredUser(pageOwner)
                    .build();
            isIgnored = ignoredUserRepository.existsByIgnoredUsersRelation(ignoredUsersRelation);
        }
        return UserInfoResponseDto.of(pageOwner, pageOwner.getId().equals(userId), isIgnored);
    }

    @Override
    public void withdraw(Long userId) {
        UserEntity userEntity = userRepository.getBy(userId);
        userEntity.withdrawInformation();
        postService.deleteAllPostByUser(userEntity);
    }

    @Override
    public void ignoreUser(Long me, Long ignoredUserId) {
        UserEntity userEntity = userRepository.getReferenceById(me);
        UserEntity pageOwner = userRepository.getBy(ignoredUserId);
        try {
            IgnoredUsersRelation ignoredUsersRelation = IgnoredUsersRelation.builder()
                    .user(userEntity)
                    .ignoredUser(pageOwner)
                    .build();
            IgnoredUsers ignoredUsers = IgnoredUsers.builder()
                    .ignoredUsersRelation(ignoredUsersRelation)
                    .build();
            ignoredUserRepository.save(ignoredUsers);
        } catch (
                DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_IGNORED_USER);
        }
    }

    @Override
    public void cancelIgnoreUser(Long me, Long ignoredUserId) {
        ignoredUserRepository.deleteIgnore(me, ignoredUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IgnoredUserResponseDto> getIgnoreUserList(Long me) {
        List<IgnoredUsers> ignoreUserListByUserId = ignoredUserRepository.getIgnoreUserListByUserId(me);
        return IgnoredUserResponseDto.of(ignoreUserListByUserId);
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}
