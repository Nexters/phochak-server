package com.nexters.phochak.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.controller.LikesController;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.Likes;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.LikesRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nexters.phochak.specification.PostCategoryEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.exception.ResCode.OK;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
class LikesIntegrationTest extends RestDocs {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenService jwtTokenService;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;
    @Autowired
    LikesController likesController;

    static String testToken;
    @Autowired
    private ShortsRepository shortsRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikesRepository likesRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, likesController).build();
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
    @DisplayName("???????????? ?????? API - ???????????? ??????")
    void addPhochak() throws Exception {
        //given
        User user = userRepository.findByNickname("nickname").orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Shorts shorts = Shorts.builder()
                .uploadKey("key")
                .shortsUrl("shortsUrl")
                .thumbnailUrl("thumbnailUrl")
                .build();
        shortsRepository.save(shorts);

        Post post = Post.builder()
                .user(user)
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        postRepository.save(post);

        //when, then
        mockMvc.perform(post("/v1/post/{postId}/likes/", post.getId()).header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/{postId}/likes/post",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(??????) ?????? ????????? Id")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ???????????? ?????? API - ?????? ???????????? ??????")
    void cancelPhochak() throws Exception {
        //given
        User user = userRepository.findByNickname("nickname").orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Shorts shorts = Shorts.builder()
                .uploadKey("key")
                .shortsUrl("shortsUrl")
                .thumbnailUrl("thumbnailUrl")
                .build();
        shortsRepository.save(shorts);

        Post post = Post.builder()
                .user(user)
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        postRepository.save(post);

        Likes likes = Likes.builder()
                        .user(user)
                        .post(post)
                        .build();
        likesRepository.save(likes);

        //when, then
        mockMvc.perform(delete("/v1/post/{postId}/likes", post.getId()).header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("post/{postId}/likes/delete",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(??????) ?????? ????????? Id")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                        )
                ));

    }

}
