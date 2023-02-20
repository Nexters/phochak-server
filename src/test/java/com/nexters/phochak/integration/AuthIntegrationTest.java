package com.nexters.phochak.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.controller.UserController;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.TokenDto;
import com.nexters.phochak.dto.request.ReissueTokenRequestDto;
import com.nexters.phochak.exception.CustomExceptionHandler;
import com.nexters.phochak.repository.RefreshTokenRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.JwtTokenService;
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

import static com.nexters.phochak.exception.ResCode.EXPIRED_TOKEN;
import static com.nexters.phochak.exception.ResCode.INVALID_TOKEN;
import static com.nexters.phochak.exception.ResCode.OK;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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

}
