package com.nexters.phochak.integration;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.service.UserService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@SpringBootTest
class UserIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @MockBean
    PostService postService;

    User user;

    @BeforeEach
    void setUp() {
        String nickname = "testUser";

        user = User.builder()
                .nickname(nickname)
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("test")
                .build();
    }

    @Test
    @DisplayName("중복된 닉네임으로 가입하면 isDuplicated가 true로 반환된다")
    void duplicatedTest() throws Exception {
        // given
        String nickname = "testUser";

        userRepository.save(user);

        // when & then
        mockMvc.perform(get("/v1/user/check/nickname")
                .param("nickname", nickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isDuplicated").value(true));
    }

    @Test
    @DisplayName("회원 탈퇴 시 탈퇴 일시가 기록된다")
    void withdrawTest() {
        // given
        userRepository.save(user);

        // when
        userService.withdraw(user.getId());

        // then
        Optional<User> findUser = userRepository.findById(user.getId());
        assertThat(findUser).isPresent();
        assertThat(findUser.get().getLeaveDate()).isNotNull();
    }
}
