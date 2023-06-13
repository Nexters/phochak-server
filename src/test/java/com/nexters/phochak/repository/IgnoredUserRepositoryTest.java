package com.nexters.phochak.repository;

import com.nexters.phochak.domain.IgnoredUsers;
import com.nexters.phochak.domain.IgnoredUsersRelation;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class IgnoredUserRepositoryTest {

    @Autowired IgnoredUserRepository ignoredUserRepository;
    @Autowired UserRepository userRepository;

    @Test
    public void save() {
        // given
        User me = User.builder()
                .id(1L)
                .nickname("me")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("000")
                .profileImgUrl("some_url0")
                .build();
        userRepository.save(me);
        User user1 = User.builder()
                .id(2L)
                .nickname("user1")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("111")
                .profileImgUrl("some_url1")
                .build();
        userRepository.save(user1);

        // when
        IgnoredUsersRelation ignoredUsersRelation = new IgnoredUsersRelation(me, user1);
        IgnoredUsers ignoredUsers = new IgnoredUsers(ignoredUsersRelation);
        ignoredUserRepository.save(ignoredUsers);

        // then
        assertTrue(ignoredUserRepository.existsByIgnoredUsersRelation(ignoredUsersRelation));
    }

    @Test
    public void getIgnoreUserListByUserId() {
        // given
        User me = User.builder()
                .id(1L)
                .nickname("me")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("1")
                .profileImgUrl("some_url1")
                .build();
        userRepository.save(me);
        User user2 = User.builder()
                .id(2L)
                .nickname("user2")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("2")
                .profileImgUrl("some_url2")
                .build();
        userRepository.save(user2);
        User user3 = User.builder()
                .id(3L)
                .nickname("user3")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("3")
                .profileImgUrl("some_url3")
                .build();
        userRepository.save(user3);

        // when
        IgnoredUsersRelation ignoredUsersRelation = new IgnoredUsersRelation(me, user2);
        IgnoredUsers ignoredUsers = new IgnoredUsers(ignoredUsersRelation);
        ignoredUserRepository.save(ignoredUsers);
        IgnoredUsersRelation ignoredUsersRelation2 = new IgnoredUsersRelation(me, user3);
        IgnoredUsers ignoredUsers2 = new IgnoredUsers(ignoredUsersRelation2);
        ignoredUserRepository.save(ignoredUsers2);

        // then
        List<IgnoredUsers> list = ignoredUserRepository.getIgnoreUserListByUserId(me.getId());
        assertEquals(2, list.size());
        assertEquals(2L, list.get(0).getIgnoredUsersRelation().getIgnoredUser().getId());
        assertEquals(3L, list.get(1).getIgnoredUsersRelation().getIgnoredUser().getId());
    }

}