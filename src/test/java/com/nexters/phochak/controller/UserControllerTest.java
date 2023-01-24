package com.nexters.phochak.controller;

import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends RestDocs {

    @Mock
    UserService userService;

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
        String code = "testCode";

        mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/v1/user/login/{provider}", provider)
                        .param("code", code)
        )
        .andExpect(status().isOk())
        .andDo(document("user/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("provider").description("OAuth 서비스 이름")
                ),
                requestParameters(
                        parameterWithName("code").description("Authorization code")
                ))
                // TODO: 로그인 완성 후 응답 mocking 필요
        );
    }
}
