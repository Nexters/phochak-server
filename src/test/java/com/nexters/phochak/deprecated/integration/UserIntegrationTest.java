//package com.nexters.phochak.deprecated.integration;
//
//import com.nexters.phochak.post.application.port.in.PostUseCase;
//import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
//import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
//import com.nexters.phochak.user.application.port.in.UserUseCase;
//import com.nexters.phochak.user.domain.OAuthProviderEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Disabled
//@Transactional
//@AutoConfigureMockMvc
//@ActiveProfiles(profiles = "test")
//@SpringBootTest
//class UserIntegrationTest {
//
//    @Autowired
//    UserUseCase userService;
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    UserRepository userRepository;
//    @MockBean
//    PostUseCase postUseCase;
//
//    UserEntity userEntity;
//
//    @BeforeEach
//    void setUp() {
//        String nickname = "testUser";
//
//        userEntity = UserEntity.builder()
//                .nickname(nickname)
//                .provider(OAuthProviderEnum.KAKAO)
//                .providerId("test")
//                .build();
//    }
//
//    @Test
//    @DisplayName("중복된 닉네임으로 가입하면 isDuplicated가 true로 반환된다")
//    void duplicatedTest() throws Exception {
//        // given
//        String nickname = "testUser";
//
//        userRepository.save(userEntity);
//
//        // when & then
//        mockMvc.perform(get("/v1/user/check/nickname")
//                .param("nickname", nickname))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.isDuplicated").value(true));
//    }
//
//    @Test
//    @DisplayName("회원 탈퇴 시 탈퇴 일시가 기록된다")
//    void withdrawTest() {
//        // given
//        userRepository.save(userEntity);
//
//        // when
//        userService.withdraw(userEntity.getId());
//
//        // then
//        Optional<UserEntity> findUser = userRepository.findById(userEntity.getId());
//        assertThat(findUser).isPresent();
//        assertThat(findUser.get().getLeaveDate()).isNotNull();
//    }
//}
