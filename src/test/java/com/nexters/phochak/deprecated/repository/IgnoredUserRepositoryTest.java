package com.nexters.phochak.deprecated.repository;

import com.nexters.phochak.ignore.domain.IgnoredUserRepository;
import com.nexters.phochak.ignore.domain.IgnoredUsers;
import com.nexters.phochak.ignore.domain.IgnoredUsersRelation;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@ActiveProfiles("test")
@SpringBootTest
class IgnoredUserRepositoryTest {

    @Autowired
    IgnoredUserRepository ignoredUserRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    @Test
    public void save() {
        // given
        UserEntity me = UserEntity.builder()
                .id(1L)
                .nickname("me")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("000")
                .profileImgUrl("some_url0")
                .build();
        userRepository.save(me);
        UserEntity userEntity1 = UserEntity.builder()
                .id(2L)
                .nickname("user1")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("111")
                .profileImgUrl("some_url1")
                .build();
        userRepository.save(userEntity1);

        // when
        IgnoredUsersRelation ignoredUsersRelation = new IgnoredUsersRelation(me, userEntity1);
        IgnoredUsers ignoredUsers = new IgnoredUsers(ignoredUsersRelation);
        ignoredUserRepository.save(ignoredUsers);

        // then
        assertTrue(ignoredUserRepository.existsByIgnoredUsersRelation(ignoredUsersRelation));
    }

    @Transactional
    @Test
    public void getIgnoreUserListByUserId() {
        // given
        UserEntity me = UserEntity.builder()
                .id(1L)
                .nickname("me")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("1")
                .profileImgUrl("some_url1")
                .build();
        userRepository.save(me);
        UserEntity userEntity2 = UserEntity.builder()
                .id(2L)
                .nickname("user2")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("2")
                .profileImgUrl("some_url2")
                .build();
        userRepository.save(userEntity2);
        UserEntity userEntity3 = UserEntity.builder()
                .id(3L)
                .nickname("user3")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("3")
                .profileImgUrl("some_url3")
                .build();
        userRepository.save(userEntity3);

        // when
        IgnoredUsersRelation ignoredUsersRelation = new IgnoredUsersRelation(me, userEntity2);
        IgnoredUsers ignoredUsers = new IgnoredUsers(ignoredUsersRelation);
        ignoredUserRepository.save(ignoredUsers);
        IgnoredUsersRelation ignoredUsersRelation2 = new IgnoredUsersRelation(me, userEntity3);
        IgnoredUsers ignoredUsers2 = new IgnoredUsers(ignoredUsersRelation2);
        ignoredUserRepository.save(ignoredUsers2);

        // then
        List<IgnoredUsers> list = ignoredUserRepository.getIgnoreUserListByUserId(me.getId());
        for (IgnoredUsers users : list) {
            System.out.println(users.getIgnoredUsersRelation().getIgnoredUser().getId());
        }
        assertEquals(2, list.size());
        assertEquals(2L, list.get(0).getIgnoredUsersRelation().getIgnoredUser().getId());
        assertEquals(3L, list.get(1).getIgnoredUsersRelation().getIgnoredUser().getId());
    }

}