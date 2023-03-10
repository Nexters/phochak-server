package com.nexters.phochak.integration;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.exception.ResCode.INVALID_INPUT;
import static com.nexters.phochak.exception.ResCode.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class PostLocalStorageIntegrationTest {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenServiceImpl jwtTokenService;
    @Autowired MockMvc mockMvc;

    static String testToken;

    @BeforeAll
    void setUp() {
        User user = User.builder()
                        .providerId("1234")
                        .provider(OAuthProviderEnum.KAKAO)
                        .nickname("nickname")
                        .profileImgUrl(null)
                        .build();
        userRepository.save(user);
        TokenDto tokenDto = jwtTokenService.generateToken(user.getId(), 999999999L);
        testToken = TokenDto.TOKEN_TYPE + " " + tokenDto.getTokenString();
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void createPost_success() throws Exception {
        // given
        MockMultipartFile testVideo = new MockMultipartFile(
                "shorts",
                "test.mov",
                "video/mov",
                new FileInputStream("app-resource/test/dummy/test.mov"));

        // when, then
        mockMvc.perform(multipart("/v1/post")
                .file(testVideo)
                .param("postCategory", "RESTAURANT")
                .param("hashtags", "[\"????????????1\", \"????????????2\", \"????????????3\"))]")
                .header(AUTHORIZATION_HEADER, testToken)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????????????? ?????? ?????? INVALID_INPUT ????????? ????????????")
    void createPostValidateEssentialParameter_InvalidInput() throws Exception {
        // given
        MockMultipartFile testVideo = new MockMultipartFile(
                "shorts",
                "test.mov",
                "video/mov",
                new FileInputStream("app-resource/test/dummy/test.mov"));

        // when, then
        mockMvc.perform(multipart("/v1/post")
                        .param("postCategory", "RESTAURANT")
                        .param("hashtags", "[\"????????????1\", \"????????????2\", \"????????????3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));

        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("hashtags", "[\"????????????1\", \"????????????2\", \"????????????3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));

        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("postCategory", "RESTAURANT")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
    }

    @Test
    @DisplayName("???????????? ?????? ???????????? ?????? ?????? INVALID_INPUT ????????? ????????????")
    void createPostValidateCategory_InvalidInput() throws Exception {
        // given
        MockMultipartFile testVideo = new MockMultipartFile(
                "shorts",
                "test.mov",
                "video/mov",
                new FileInputStream("app-resource/test/dummy/test.mov"));

        // when, then
        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("postCategory", "INVALIDCATEGORY")
                        .param("hashtags", "[\"????????????1\", \"????????????2\", \"????????????3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
    }

    @Test
    @DisplayName("??????????????? ????????? 30?????? ?????????, INVALID_INPUT ????????? ????????????")
    void HashtagOver30_InvalidInput() throws Exception {
        // given
        MockMultipartFile testVideo = new MockMultipartFile(
                "shorts",
                "test.mov",
                "video/mov",
                new FileInputStream("app-resource/test/dummy/test.mov"));

        StringBuilder hashtagStringList = new StringBuilder("[");
        for(int i=0;i<31;i++) {
            hashtagStringList.append("\"????????????").append(i).append("\",");
        }
        hashtagStringList.deleteCharAt(hashtagStringList.length() - 1);
        hashtagStringList.append("]");

        // when, then
        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("postCategory", "RESTAURANT")
                        .param("hashtags", hashtagStringList.toString())
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
    }
}
