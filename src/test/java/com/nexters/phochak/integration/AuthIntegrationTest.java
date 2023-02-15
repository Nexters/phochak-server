package com.nexters.phochak.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.controller.UserController;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.request.ReissueTokenRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.RefreshTokenRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.impl.JwtTokenServiceImpl;
import com.nexters.phochak.specification.OAuthProviderEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import java.util.HashMap;
import java.util.Map;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.exception.ResCode.INVALID_INPUT;
import static com.nexters.phochak.exception.ResCode.OK;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTest extends RestDocs {

    @Autowired UserController userController;
    @Autowired UserRepository userRepository;
    @Autowired JwtTokenService jwtTokenService;
    @Autowired ObjectMapper objectMapper;
    @Autowired RefreshTokenRepository refreshTokenRepository;
    MockMvc mockMvc;
    static String testToken;
    static Long globalUserId;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userController).build();
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
    void addPhochak() throws Exception {
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

}
