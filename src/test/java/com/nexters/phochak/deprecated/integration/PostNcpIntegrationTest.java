package com.nexters.phochak.deprecated.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.docs.RestDocs;
import com.nexters.phochak.common.exception.CustomExceptionHandler;
import com.nexters.phochak.post.adapter.in.web.PostController;
import com.nexters.phochak.post.adapter.out.api.ReportNotificationFeignClient;
import com.nexters.phochak.post.adapter.out.persistence.HashtagEntity;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.adapter.out.persistence.ReportPostRepository;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.shorts.presentation.NCPStorageClient;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.JwtTokenService;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.INVALID_INPUT;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class PostNcpIntegrationTest extends RestDocs {

    @Autowired UserRepository userRepository;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    PostController postController;
    @Autowired EntityManager em;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;
    static String testToken;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ShortsRepository shortsRepository;
    @MockBean NCPStorageClient ncpStorageClient;
    @MockBean
    ReportNotificationFeignClient slackPostReportFeignClient;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private ReportPostRepository reportPostRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController)
                .setControllerAdvice(CustomExceptionHandler.class)
                .build();
        UserEntity userEntity = UserEntity.builder()
                .providerId("1234")
                .provider(OAuthProviderEnum.KAKAO)
                .nickname("nickname")
                .profileImgUrl(null)
                .build();
        userRepository.save(userEntity);
        JwtTokenUseCase.TokenVo tokenDto = jwtTokenService.generateToken(userEntity.getId(), 999999999L);
        testToken = JwtTokenUseCase.TokenVo.TOKEN_TYPE + " " + tokenDto.getTokenString();
    }

    @Test
    @DisplayName("포스트 API - upload key 생성 성공")
    void uploadKey_success() throws Exception {
        //given
        given(ncpStorageClient.generatePresignedUrl(any())).willReturn(new URL("http://test.com"));

        // when, then
        mockMvc.perform(get("/v1/post/upload-key")
                        .queryParam("file-extension", "mov")
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/upload-key/GET",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("file-extension").description("파일 생성자 ex) mp4")
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
    @DisplayName("포스트 API - 게시글 생성 성공")
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
    @DisplayName("포스트 API - 게시글 수정 성공")
    void updatePost_success() throws Exception {
        //given
        UserEntity userEntity = userRepository.findByNickname("nickname").get();

        Shorts shorts = Shorts.builder()
                .thumbnailUrl("test")
                .shortsUrl("test")
                .uploadKey("test")
                .build();
        shortsRepository.save(shorts);

        PostEntity postEntity = PostEntity.builder()
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .userEntity(userEntity)
                .build();
        postRepository.save(postEntity);
        Long postId = postEntity.getId();

        List<HashtagEntity> hashtagEntities = List.of(new HashtagEntity(postEntity, "hashtag1"), new HashtagEntity(postEntity, "hashtag2"), new HashtagEntity(postEntity, "hashtag3"));
        hashtagRepository.saveAll(hashtagEntities);

        em.flush();
        em.clear();

        Map<String, Object> body = new HashMap<>();
        body.put("category", "RESTAURANT");
        body.put("hashtags", List.of("해시태그1", "해시태그2"));

        // when, then
        mockMvc.perform(put("/v1/post/{postId}", postId)
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/PUT",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(필수) 수정할 포스트의 id")
                        ),
                        requestFields(
                                fieldWithPath("category").description("카테고리 ex) TOUR / RESTAURANT / CAFE"),
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
    @DisplayName("포스트 API - 게시글 삭제 성공")
    void deletePost_success() throws Exception {
        //given
        UserEntity userEntity = userRepository.findByNickname("nickname").get();

        Shorts shorts = Shorts.builder()
                .thumbnailUrl("test")
                .shortsUrl("test")
                .uploadKey("test")
                .build();
        shortsRepository.save(shorts);

        PostEntity postEntity = PostEntity.builder()
                        .shorts(shorts)
                        .postCategory(PostCategoryEnum.TOUR)
                        .userEntity(userEntity)
                        .build();
        postRepository.save(postEntity);
        Long postId = postEntity.getId();

        List<HashtagEntity> hashtagEntities = List.of(new HashtagEntity(postEntity, "hashtag1"), new HashtagEntity(postEntity, "hashtag2"), new HashtagEntity(postEntity, "hashtag3"));
        hashtagRepository.saveAll(hashtagEntities);

        em.flush();
        em.clear();

        // when, then
        mockMvc.perform(delete("/v1/post/{postId}", postId)
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/DELETE",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(필수) 삭제할 포스트의 id")
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
        Assertions.assertThat(postRepository.count()).isZero();
        Assertions.assertThat(shortsRepository.count()).isZero();
    }

    @Test
    @DisplayName("포스트 API - 게시글 작성 필수 파라미터가 없는 경우 INVALID_INPUT 예외가 발생한다")
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
    @DisplayName("포스트 API - 존재하지 않는 카테고리 입력 시에 INVALID_INPUT 예외가 발생한다")
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
    @DisplayName("포스트 API - 해시태그의 개수가 30개가 넘으면, INVALID_INPUT 예외가 발생한다")
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

    @Test
    @DisplayName("포스트 API - 게시글 신고하기 성공")
    void reportPost() throws Exception {
        UserEntity userEntity = userRepository.findByNickname("nickname").get();

        Shorts shorts = Shorts.builder()
                .thumbnailUrl("test")
                .shortsUrl("test")
                .uploadKey("test")
                .build();
        shortsRepository.save(shorts);

        PostEntity postEntity = PostEntity.builder()
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .userEntity(userEntity)
                .build();
        postRepository.save(postEntity);
        Long postId = postEntity.getId();

        // when, then
        mockMvc.perform(post("/v1/post/{postId}/report", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/report",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(필수) 신고할 포스트의 id")
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
}
