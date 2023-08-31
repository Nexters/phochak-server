package com.nexters.phochak.common;

import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import org.springframework.test.web.servlet.MockMvc;

public class TestUtil {
    public static UserRepository userRepository;
    public static MockMvc mockMvc;
    public static JwtTokenUseCase jwtTokenUseCase;
    public static class TestUser {
        public static Long userId = 1L;
        public static String accessToken;
    }


    public static void setUserRepository(final UserRepository userRepository) {
        TestUtil.userRepository = userRepository;
    }

    public static void setJwtTokenUseCase(final JwtTokenUseCase jwtTokenUseCase) {
        TestUtil.jwtTokenUseCase = jwtTokenUseCase;
    }

    public static void setMockMvc(final MockMvc mockMvc) {
        TestUtil.mockMvc = mockMvc;
    }

    public static void setAccessToken(final String accessToken) {
        TestUtil.TestUser.accessToken = accessToken;
    }

}
