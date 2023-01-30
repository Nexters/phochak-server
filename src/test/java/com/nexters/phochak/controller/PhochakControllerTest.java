package com.nexters.phochak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.Phochak;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PhochakRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nexters.phochak.specification.PostCategoryEnum;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional
class PhochakControllerTest extends RestDocs {

    @Autowired UserRepository userRepository;
    @Autowired JwtTokenService jwtTokenService;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;
    @Autowired PhochakController phochakController;

    static String testToken;
    @Autowired
    private ShortsRepository shortsRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PhochakRepository phochakRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, phochakController).build();
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
    @DisplayName("포착하기 요청 API - 포착하기 성공")
    void addPhochak() throws Exception {
        //given
        User user = userRepository.findOneByNickname("nickname").orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Shorts shorts = Shorts.builder()
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
        mockMvc.perform(post("/v1/post/{postId}/phochak/", post.getId()).header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(OK.getCode()))
                .andDo(document("post/{postId}/phochak/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("해당 게시글 Id")
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
    @DisplayName("포착 취소하기 요청 API - 포착 취소하기 성공")
    void cancelPhochak() throws Exception {
        //given
        User user = userRepository.findOneByNickname("nickname").orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_USER));
        Shorts shorts = Shorts.builder()
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

        Phochak phochak = Phochak.builder()
                        .user(user)
                        .post(post)
                        .build();
        phochakRepository.save(phochak);

        //when, then
        mockMvc.perform(delete("/v1/post/{postId}/phochak", post.getId()).header(AUTHORIZATION_HEADER, testToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resCode").value(OK.getCode()))
                .andDo(document("post/{postId}/phochak/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("해당 게시글 Id")
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

}