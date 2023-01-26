package com.nexters.phochak.controller;

import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.exception.ResCode.INVALID_INPUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenServiceImpl jwtTokenService;
    @Autowired MockMvc mockMvc;
    @Value("${app.resource.local.shorts}") String shortsPath;

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
        TokenDto tokenDto = jwtTokenService.generateAccessToken(user.getId());
        testToken = TokenDto.TOKEN_TYPE + " " + tokenDto.getTokenString();
    }

    @AfterAll
    void removeTestVideo() {
        File deleteFolder = new File(shortsPath);

        if(deleteFolder.exists()){
            File[] deleteFolderList = deleteFolder.listFiles();

            for (File file : deleteFolderList) {
                file.delete();
            }

            if(deleteFolderList.length == 0 && deleteFolder.isDirectory()){
                deleteFolder.delete();
            }
        }
    }

    @Test
    @DisplayName("게시글 생성 성공")
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
                .param("hashtags", "[\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                .header(AUTHORIZATION_HEADER, testToken)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 작성 필수 파라미터가 없는 경우 INVALID_INPUT 예외가 발생한다")
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
                        .param("hashtags", "[\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));;

        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("hashtags", "[\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));;

        mockMvc.perform(multipart("/v1/post")
                        .file(testVideo)
                        .param("postCategory", "RESTAURANT")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));;
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 입력 시에 INVALID_INPUT 예외가 발생한다")
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
                        .param("hashtags", "[\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));;
    }

    //TODO: 해시테그 정책 확정 후 테스트 추가

}
