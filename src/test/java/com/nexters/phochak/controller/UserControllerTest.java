package com.nexters.phochak.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.dto.response.IgnoredUserResponseDto;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @DisplayName("유저 API - 로그인 성공")
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
                                        parameterWithName("provider").description("(필수) OAuth 서비스 이름(ex. kakao, apple, naver)")
                                ),
                                requestParameters(
                                        parameterWithName("token").description("(필수) token (Access token or Identify Token)")
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
    @DisplayName("유저 API - 닉네임 중복확인")
    void checkNicknameIsDuplicated() throws Exception {
        String nickname = "여행자#123";
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
                                parameterWithName("nickname").description("(필수) 중복확인하고자 하는 닉네임 ('#' 때문에 URL 인코딩 처리해주세요)")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.isDuplicated").type(JsonFieldType.BOOLEAN).description("닉네임 중복여부")
                        )
                ));
    }

    @Test
    @DisplayName("유저 API - 닉네임 변경하기")
    void modifyNickname() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String nickname = "변경 닉네임";
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
                                fieldWithPath("nickname").description("(필수) 변경하고자 하는 닉네임")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
                        )
                ));
    }

    @Test
    @DisplayName("유저 API - 다른 유저 페이지 정보 조회하기")
    void getInfoOtherUserPage() throws Exception {
        UserInfoResponseDto response = UserInfoResponseDto.builder()
                .id(1L)
                .nickname("nickname")
                .profileImgUrl("profile image url")
                .isMyPage(false)
                .isIgnored(true)
                .isBlocked(false)
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
                                        .description("(필수) JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("(선택) 조회하려는 유저의 id. 만약 본인의 유저 페이지를 조회 한다면 빈 값 사용")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 식별값(id)"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("프로필 이미지 링크"),
                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("접속자가 해당 페이지의 주인인지 확인"),
                                fieldWithPath("data.isBlocked").type(JsonFieldType.BOOLEAN).description("해당 유저 차단 여부"),
                                fieldWithPath("data.isIgnored").type(JsonFieldType.BOOLEAN).description("해당 유저 무시 여부")
                        )
                ));
    }

    @Test
    @DisplayName("유저 API - 본인 유저 페이지 정보 조회하기")
    void getInfoMyPage() throws Exception {
        UserInfoResponseDto response = UserInfoResponseDto.builder()
                .id(1L)
                .nickname("nickname")
                .profileImgUrl("profile image url")
                .isMyPage(true)
                .isIgnored(false)
                .isBlocked(false)
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
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 식별값(id)"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("data.profileImgUrl").type(JsonFieldType.STRING).description("프로필 이미지 링크"),
                                fieldWithPath("data.isMyPage").type(JsonFieldType.BOOLEAN).description("접속자가 해당 페이지의 주인인지 확인 (항상 true)"),
                                fieldWithPath("data.isBlocked").type(JsonFieldType.BOOLEAN).description("해당 유저 차단 여부 (항상 false)"),
                                fieldWithPath("data.isIgnored").type(JsonFieldType.BOOLEAN).description("해당 유저 무시 여부 (항상 false)")
                        )
                ));
    }

    @Test
    @DisplayName("유저 API - 유저 무시하기")
    void ignoreUser() throws Exception {
        //given
        doNothing().when(userService).ignoreUser(any(), any());
        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .post("/v1/user/ignore/{ignoredUserId}", 10)
                                .header(AUTHORIZATION_HEADER, "access token")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/ignore/POST",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("ignoredUserId").description("(필수) 무시하기 하려는 유저의 id 설정")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
                        )
                ));
    }

    @Test
    @DisplayName("유저 API - 유저 무시하기 취소")
    void cancelIgnoreUser() throws Exception {
        //given
        doNothing().when(userService).cancelIgnoreUser(any(), any());
        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .delete("/v1/user/ignore/{ignoredUserId}", 10)
                                .header(AUTHORIZATION_HEADER, "access token")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/ignore/DELETE",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("ignoredUserId").description("(필수) 무시하기 했던 유저의 id 설정")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
                        )
                ));

    }

    @Test
    @DisplayName("유저 API - 무시하기한 유저 목록 조회")
    void getIgnoreUser() throws Exception {
        //given
        List<IgnoredUserResponseDto> response = new ArrayList<>();
        response.add(IgnoredUserResponseDto.builder()
                .id(10L)
                .nickname("nickname10")
                .profileImgUrl("profile_image_url10")
                .build());
        when(userService.getIgnoreUserList(any())).thenReturn(response);
        //when, then
        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/ignore", 10)
                                .header(AUTHORIZATION_HEADER, "access token")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user/ignore/GET",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("무시한 유저 id"),
                                fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("무시한 유저 닉네임"),
                                fieldWithPath("data[].profileImgUrl").type(JsonFieldType.STRING).description("무시한 유저 프로필 이미지 링크")
                        )
                ));

    }
}
