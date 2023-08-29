package com.nexters.phochak.deprecated.controller;

import com.nexters.phochak.auth.adapter.in.web.AuthController;
import com.nexters.phochak.common.docs.RestDocs;
import com.nexters.phochak.user.application.port.in.JwtResponseDto;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.application.port.in.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@ExtendWith(MockitoExtension.class)
class UserV2ControllerTest extends RestDocs {

    @Mock
    UserUseCase userService;
    @Mock
    JwtTokenUseCase jwtTokenUseCase;

    @InjectMocks
    AuthController userV2Controller;
    MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userV2Controller).build();
    }

    @Test
    @DisplayName("유저 API V2 - 로그인 성공")
    void loginV2() throws Exception {
        String provider = "kakao";
        String token = "testCode";

        JwtResponseDto response = JwtResponseDto.builder()
                .accessToken("Bearer {jwt}")
                .expiresIn("access token lifetime(ms)")
                .refreshToken("Bearer {jwt}")
                .refreshTokenExpiresIn("refresh token lifetime(ms)")
                .build();

        when(jwtTokenUseCase.issueToken(any())).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v2/auth/login/{provider}", provider)
                                .param("token", token)
                                .param("fcmDeviceToken", "TestFcmDeviceToken")
                )
                .andExpect(status().isOk())
                .andDo(document("v2/auth/login",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("provider").description("(필수) OAuth 서비스 이름(ex. kakao, apple, naver)")
                        ),
                        requestFields(
                                fieldWithPath("token").description("(필수) token (Access token or Identify Token)"),
                                fieldWithPath("fcmDeviceToken").description("(필수) FCM client 식별 토큰")
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
