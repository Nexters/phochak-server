package com.nexters.phochak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.exception.ResCode.INVALID_INPUT;
import static com.nexters.phochak.exception.ResCode.OK;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostNcpControllerTest {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenServiceImpl jwtTokenService;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
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
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("key", "key");
        body.put("postCategory", "RESTAURANT");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                .content(objectMapper.writeValueAsString(body))
                .contentType("application/json")
                .header(AUTHORIZATION_HEADER, testToken)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.resCode").value(OK.getCode()))
        .andDo(document("/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("uploadKey").description("발급받았던 업로드 키"),
                        parameterWithName("postCategory").description("카테고리 ex) TOUR/RESTAURANT"),
                        parameterWithName("hashtags").description("해시태그 배열 ex) [\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                )
        ));
    }

    @Test
    @DisplayName("게시글 작성 필수 파라미터가 없는 경우 INVALID_INPUT 예외가 발생한다")
    void createPostValidateEssentialParameter_InvalidInput() throws Exception {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("postCategory", "RESTAURANT");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));

        //given
        body = new HashMap<>();
        body.put("key", "key");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));

        //given
        body = new HashMap<>();
        body.put("key", "key");
        body.put("postCategory", "RESTAURANT");

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 입력 시에 INVALID_INPUT 예외가 발생한다")
    void createPostValidateCategory_InvalidInput() throws Exception {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("key", "key");
        body.put("postCategory", "INVALIDCATEGORY");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));
    }

    @Test
    @DisplayName("해시태그의 개수가 30개가 넘으면, INVALID_INPUT 예외가 발생한다")
    void HashtagOver30_InvalidInput() throws Exception {
        //given
        List<String> hashtagList = new ArrayList<>();
        for(int i=0;i<31;i++) {
            hashtagList.add("해시태그"+i);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("key", "key");
        body.put("postCategory", "RESTAURANT");
        body.put("hashtags", hashtagList);

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType("application/json")
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(INVALID_INPUT.getCode()));
    }
}
