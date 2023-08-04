package com.nexters.phochak.controller;

import com.nexters.phochak.controller.v2.UserV2Controller;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.dto.response.JwtResponseDto;
import com.nexters.phochak.service.JwtTokenService;
import com.nexters.phochak.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserV2ControllerTest extends RestDocs {

    @Mock
    UserService userService;
    @Mock
    JwtTokenService jwtTokenService;

    @InjectMocks
    UserV2Controller userV2Controller;
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

        when(jwtTokenService.issueToken(any())).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v2/user/login/{provider}", provider)
                                .param("token", token)
                                .param("fcmDeviceToken", "TestFcmDeviceToken")
                )
                .andExpect(status().isOk())
                .andDo(document("v2/user/login",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("provider").description("(필수) OAuth 서비스 이름(ex. kakao, apple, naver)")
                        ),
                        requestParameters(
                                parameterWithName("token").description("(필수) token (Access token or Identify Token)"),
                                parameterWithName("fcmDeviceToken").description("(필수) FCM client 식별 토큰")
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
