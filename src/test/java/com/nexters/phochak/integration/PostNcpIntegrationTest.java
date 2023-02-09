package com.nexters.phochak.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.controller.PostController;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.exception.CustomExceptionHandler;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
public class PostNcpIntegrationTest extends RestDocs {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenServiceImpl jwtTokenService;
    @Autowired
    PostController postController;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;
    static String testToken;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController)
                .setControllerAdvice(CustomExceptionHandler.class)
                .build();
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

    @Test
    @DisplayName("upload key 생성 성공")
    void uploadKey_success() throws Exception {

        // when, then
        mockMvc.perform(get("/v1/post/upload-key")
                        .queryParam("file-extension", "mov")
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/upload-key/GET",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("file-extension").description("파일 생성자 ex) mp4")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.uploadUrl").type(JsonFieldType.STRING).description("파일 업로드 URL"),
                                fieldWithPath("data.uploadKey").type(JsonFieldType.STRING).description("업로드 키")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 생성 성공")
    void createPost_success() throws Exception {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("uploadKey", "uploadKey");
        body.put("category", "RESTAURANT");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
            .content(objectMapper.writeValueAsString(body))
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION_HEADER, testToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
            .andDo(document("post/POST",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("category").description("카테고리 ex) TOUR / RESTAURANT / CAFE"),
                        fieldWithPath("uploadKey").description("발급 받았던 업로드 키"),
                        fieldWithPath("hashtags").description("해시태그 배열 ex) [\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                )
        ));
    }

    @Test
    @DisplayName("게시글 작성 필수 파라미터가 없는 경우 INVALID_INPUT 예외가 발생한다")
    void createPostValidateEssentialParameter_InvalidInput() throws Exception {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("category", "RESTAURANT");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));

        //given
        body = new HashMap<>();
        body.put("uploadKey", "uploadKey");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));

        //given
        body = new HashMap<>();
        body.put("uploadKey", "uploadKey");
        body.put("category", "RESTAURANT");

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 입력 시에 INVALID_INPUT 예외가 발생한다")
    void createPostValidateCategory_InvalidInput() throws Exception {
        //given
        Map<String, Object> body = new HashMap<>();
        body.put("key", "key");
        body.put("category", "INVALIDCATEGORY");
        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
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
        body.put("category", "RESTAURANT");
        body.put("hashtags", hashtagList);

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_INPUT.getCode()));
    }
}
