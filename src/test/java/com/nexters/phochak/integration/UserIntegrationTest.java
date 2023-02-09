package com.nexters.phochak.integration;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.UserService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @DisplayName("중복된 닉네임으로 가입하면 isDuplicated가 true로 반환된다")
    void duplicatedTest() throws Exception {
        // given
        String nickname = "testUser";

        User user = User.builder()
                .id(1L)
                .nickname(nickname)
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("test")
                .build();

        userRepository.save(user);

        // when & then
        mockMvc.perform(get("/v1/user/check/nickname")
                .param("nickname", nickname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isDuplicated").value(true));
    }
}
