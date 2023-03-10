package com.nexters.phochak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.dto.response.UserCheckResponseDto;
import com.nexters.phochak.dto.response.UserInfoResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
    @DisplayName("?????? API - ????????? ??????")
    void login() throws Exception {
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
                                .get("/v1/user/login/{provider}", provider)
                                .param("token", token)
                )
                .andExpect(status().isOk())
                .andDo(document("user/login",
                                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("provider").description("(??????) OAuth ????????? ??????(ex. kakao, apple, naver)")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("(??????) token (Access token or Identify Token)")
                                ),
                                responseFields(
                                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("access token"),
                                        fieldWithPath("data.expiresIn").type(JsonFieldType.STRING).description("access token ????????????(ms)"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("refresh token"),
                                        fieldWithPath("data.refreshTokenExpiresIn").type(JsonFieldType.STRING).description("refresh token ????????????(ms)")
                                )
                ));
    }

    @Test
    @DisplayName("?????? API - ????????? ????????????")
    void checkNicknameIsDuplicated() throws Exception {
        String nickname = "?????????#123";
        UserCheckResponseDto response = UserCheckResponseDto.of(true);

        when(userService.checkNicknameIsDuplicated(any())).thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/check/nickname")
                                .param("nickname", nickname)
                )
                .andExpect(status().isOk())
                .andDo(document("user/check/nickname",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("nickname").description("(??????) ????????????????????? ?????? ????????? ('#' ????????? URL ????????? ??????????????????)")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data.isDuplicated").type(JsonFieldType.BOOLEAN).description("????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? API - ????????? ????????????")
    void modifyNickname() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String nickname = "?????? ?????????";
        Map<String, Object> body = new HashMap<>();
        body.put("nickname", nickname);

        doNothing().when(userService).modifyNickname(any());

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .put("/v1/user/nickname")
                                .header(AUTHORIZATION_HEADER, "access token")
                                .content(objectMapper.writeValueAsString(body))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/modify/nickname",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").description("(??????) ??????????????? ?????? ?????????")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? API - ?????? ?????? ????????? ?????? ????????????")
    void getInfoOtherUserPage() throws Exception {
        UserInfoResponseDto response = UserInfoResponseDto.builder()
                .isMyPage(false)
                .id(1L)
                .nickname("nickname")
                .profileImgUrl("profile image url")
                .build();

        given(userService.getInfo(any(),any())).willReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/{userId}", 10)
                                .header(AUTHORIZATION_HEADER, "access token"))
                .andExpect(status().isOk())
                .andDo(document("user",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("(??????) ??????????????? ????????? id. ?????? ????????? ?????? ???????????? ?????? ????????? ??? ??? ??????")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ???????????? ???????????? ??????"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("?????? ?????????(id)"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? API - ?????? ?????? ????????? ?????? ????????????")
    void getInfoMyPage() throws Exception {
        UserInfoResponseDto response = UserInfoResponseDto.builder()
                .isMyPage(true)
                .id(1L)
                .nickname("nickname")
                .profileImgUrl("profile image url")
                .build();

        given(userService.getInfo(any(),any())).willReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/")
                                .header(AUTHORIZATION_HEADER, "access token"))
                .andExpect(status().isOk())
                .andDo(document("user/me",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("???????????? ?????? ???????????? ???????????? ??????"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("?????? ?????????(id)"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("????????? ????????? ??????")
                        )
                ));
    }
}
