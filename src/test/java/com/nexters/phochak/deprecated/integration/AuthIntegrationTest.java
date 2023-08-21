package com.nexters.phochak.deprecated.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.auth.ReissueTokenRequestDto;
import com.nexters.phochak.auth.TokenDto;
import com.nexters.phochak.auth.application.JwtTokenService;
import com.nexters.phochak.auth.domain.RefreshTokenRepository;
import com.nexters.phochak.auth.presentation.LogoutRequestDto;
import com.nexters.phochak.auth.presentation.WithdrawRequestDto;
import com.nexters.phochak.common.exception.CustomExceptionHandler;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.hashtag.domain.Hashtag;
import com.nexters.phochak.hashtag.domain.HashtagRepository;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.shorts.presentation.NCPStorageClient;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import com.nexters.phochak.user.domain.UserRepository;
import com.nexters.phochak.user.presentation.UserController;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Deprecated
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTest extends RestDocs {

    @Autowired UserController userController;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired ShortsRepository shortsRepository;
    @Autowired HashtagRepository hashtagRepository;
    @Autowired JwtTokenService jwtTokenService;
    @Autowired ObjectMapper objectMapper;
    @Autowired EntityManager em;
    @Autowired RefreshTokenRepository refreshTokenRepository;
    @MockBean NCPStorageClient ncpStorageClient;
    MockMvc mockMvc;
    static Long globalUserId;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userController)
                .setControllerAdvice(CustomExceptionHandler.class)
                .build();
        User user = User.builder()
                .providerId("1234")
                .provider(OAuthProviderEnum.KAKAO)
                .nickname("nickname")
                .profileImgUrl(null)
                .build();
        userRepository.save(user);
        globalUserId = user.getId();
    }

    private static String createTokenStringForResponse(TokenDto accessToken) {
        return TokenDto.TOKEN_TYPE + " " + accessToken.getTokenString();
    }

    @Test
    @DisplayName("토큰 재발급 API - 재발급 성공")
    void reissueToken() throws Exception {
        //given
        TokenDto currentAT = jwtTokenService.generateToken(globalUserId, -1000L);
        TokenDto currentRT = jwtTokenService.generateToken(globalUserId, 9999999999L);

        refreshTokenRepository.saveWithAccessToken(currentRT.getTokenString(), currentAT.getTokenString());

        ReissueTokenRequestDto body = new ReissueTokenRequestDto(
                        createTokenStringForResponse(currentAT),
                        createTokenStringForResponse(currentRT));

        //when, then
        mockMvc.perform(post("/v1/user/reissue-token")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                    .andDo(document("user/reissue-token/",
                    preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                            fieldWithPath("accessToken").description("(필수) 만료된 Access token"),
                            fieldWithPath("refreshToken").description("(필수) 만료되지 않은 Refresh token")
                    ),
                    responseFields(
                            fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                            fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                            fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("access token"),
                            fieldWithPath("data.expiresIn").type(JsonFieldType.STRING).description("access token 유효기간(ms)"),
                            fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refresh token"),
                            fieldWithPath("data.refreshTokenExpiresIn").type(JsonFieldType.STRING).description("refresh token 유효기간(ms)")
                    )
            ));
    }

    @Test
    @DisplayName("토큰 재발급 API - Refresh Token이 만료된 경우, Expired Token 예외가 발생한다")
    void reissueToken_RTExpired_InvalidToken() throws Exception {
        //given
        TokenDto currentAT = jwtTokenService.generateToken(globalUserId, -2000L);
        TokenDto currentRT = jwtTokenService.generateToken(globalUserId, -1000L);

        refreshTokenRepository.saveWithAccessToken(currentRT.getTokenString(), currentAT.getTokenString());

        ReissueTokenRequestDto body = new ReissueTokenRequestDto(
                createTokenStringForResponse(currentAT),
                createTokenStringForResponse(currentRT));

        //when, then
        mockMvc.perform(post("/v1/user/reissue-token")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(EXPIRED_TOKEN.getCode()));
    }

    @Test
    @DisplayName("토큰 재발급 API - Access Token이 아직 만료되지 않은 경우, 탈취로 간주하고 Invalid Token 예외가 발생한다")
    void reissueToken_ATNotExpired_InvalidToken() throws Exception {
        //given
        TokenDto currentAT = jwtTokenService.generateToken(globalUserId, 1000000000L);
        TokenDto currentRT = jwtTokenService.generateToken(globalUserId, 9999999999L);

        refreshTokenRepository.saveWithAccessToken(currentRT.getTokenString(), currentAT.getTokenString());

        ReissueTokenRequestDto body = new ReissueTokenRequestDto(
                createTokenStringForResponse(currentAT),
                createTokenStringForResponse(currentRT));

        //when, then
        mockMvc.perform(post("/v1/user/reissue-token")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_TOKEN.getCode()));
    }

    @Test
    @DisplayName("토큰 재발급 API - AT와 RT가 매치되지 않는 경우, 탈취로 간주하고 Invalid Token 예외가 발생한다")
    void reissueToken_ATRTNotMatched_InvalidToken() throws Exception {
        //given
        TokenDto currentAT1 = jwtTokenService.generateToken(globalUserId, 1000000000L);
        TokenDto currentRT1 = jwtTokenService.generateToken(globalUserId, 9999999999L);

        refreshTokenRepository.saveWithAccessToken(currentRT1.getTokenString(), currentAT1.getTokenString());

        TokenDto currentAT2 = jwtTokenService.generateToken(globalUserId, 1000000000L);
        TokenDto currentRT2 = jwtTokenService.generateToken(globalUserId, 9999999999L);

        refreshTokenRepository.saveWithAccessToken(currentRT2.getTokenString(), currentAT2.getTokenString());


        ReissueTokenRequestDto body = new ReissueTokenRequestDto(
                createTokenStringForResponse(currentAT1),
                createTokenStringForResponse(currentRT2));

        //when, then
        mockMvc.perform(post("/v1/user/reissue-token")
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(INVALID_TOKEN.getCode()));
    }

    @Test
    @DisplayName("로그아웃 API - 로그아웃 성공")
    void logout_success() throws Exception {
        //given
        TokenDto currentAT1 = jwtTokenService.generateToken(globalUserId, 1000000000L);
        TokenDto currentRT1 = jwtTokenService.generateToken(globalUserId, 9999999999L);

        refreshTokenRepository.saveWithAccessToken(currentRT1.getTokenString(), currentAT1.getTokenString());

        LogoutRequestDto body = new LogoutRequestDto("Bearer " + currentRT1.getTokenString());

        //when, then
        mockMvc.perform(post("/v1/user/logout")
                        .header(AUTHORIZATION_HEADER, TokenDto.TOKEN_TYPE + " " + currentAT1.getTokenString())
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("user/logout",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").description("(필수) 만료되지 않은 Refresh token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                        )));
    }

    @Test
    @DisplayName("회원탈퇴 API - 회원탈퇴 성공")
    void withdraw_success() throws Exception {
        //given
        TokenDto currentAT = jwtTokenService.generateToken(globalUserId, 1000000000L);
        TokenDto currentRT = jwtTokenService.generateToken(globalUserId, 9999999999L);

        User user = userRepository.findById(globalUserId).get();

        for (int i=0; i < 10; i++) {
            Shorts shorts = Shorts.builder()
                    .thumbnailUrl("test" + i)
                    .shortsUrl("test" + i)
                    .uploadKey("test" + i)
                    .build();
            shortsRepository.save(shorts);

            Post post = Post.builder()
                    .shorts(shorts)
                    .postCategory(PostCategoryEnum.TOUR)
                    .user(user)
                    .build();
            postRepository.save(post);

            List<Hashtag> hashtags = List.of(new Hashtag(post, "hashtag1"), new Hashtag(post, "hashtag2"), new Hashtag(post, "hashtag3"));
            hashtagRepository.saveAll(hashtags);
        }

        refreshTokenRepository.saveWithAccessToken(currentRT.getTokenString(), currentAT.getTokenString());

        WithdrawRequestDto body = new WithdrawRequestDto("Bearer " + currentRT.getTokenString());

        //when, then
        mockMvc.perform(post("/v1/user/withdraw")
                        .header(AUTHORIZATION_HEADER, TokenDto.TOKEN_TYPE + " " + currentAT.getTokenString())
                        .content(objectMapper.writeValueAsString(body))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()))
                .andDo(document("user/withdraw",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("JWT Access Token")
                        ),
                        requestFields(
                                fieldWithPath("refreshToken").description("(필수) 만료되지 않은 Refresh token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                        )));

        em.flush();
        em.clear();

        Assertions.assertThat(userRepository.findById(globalUserId).get().getNickname()).isNull();
        Assertions.assertThat(postRepository.count()).isZero();
        Assertions.assertThat(shortsRepository.count()).isZero();
        Assertions.assertThat(hashtagRepository.count()).isZero();
    }

}
