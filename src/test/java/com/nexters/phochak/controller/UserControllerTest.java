package com.nexters.phochak.controller;

import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.dto.response.LoginResponseDto;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends RestDocs {

    @Mock
    UserService userService;
    @Mock
    JwtTokenService jwtTokenService;
    @InjectMocks
    UserController userController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, userController).build();
    }

    @Test
    @DisplayName("로그인 요청 API - 로그인 성공")
    void login() throws Exception {
        String provider = "kakao";
        String token = "testCode";

        LoginResponseDto response = LoginResponseDto.builder()
                .accessToken("Bearer {jwt}")
                .expiresIn("access token lifetime(ms)")
                .refreshToken("Bearer {jwt}")
                .refreshTokenExpiresIn("refresh token lifetime(ms)")
                .build();

        when(jwtTokenService.createLoginResponse(any())).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/login/{provider}", provider)
                                .param("token", token)
                )
                .andExpect(status().isOk())
                .andDo(document("user/login",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("provider").description("OAuth 서비스 이름(ex. kakao, apple, naver)")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("token (Access token or Identify Token)")
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
