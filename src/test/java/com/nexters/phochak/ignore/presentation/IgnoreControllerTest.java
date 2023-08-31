package com.nexters.phochak.ignore.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.ignore.adapter.in.web.IgnoreUserController;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserEntityRelation;
import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserRepository;
import com.nexters.phochak.user.adapter.out.api.KakaoInformationFeignClient;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.TestUtil.TestUser.accessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IgnoreControllerTest extends RestDocsApiTest {

    @Autowired
    IgnoreUserController ignoreController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IgnoredUserRepository ignoredUserRepository;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    KakaoInformationFeignClient kakaoInformationFeignClient;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, ignoreController).build();
        TestUtil.setMockMvc(mockMvc);
    }

    @Test
    @DisplayName("[유저 무시 API] - 유저 무시하기")
    void ignoreUser() throws Exception {
        //given
        final long ignoredUserId = 2L;
        Scenario.createUser().id(ignoredUserId).request();

        //when
        Scenario.ignoreUser().request().getResponse();

        //then
        final UserEntity userEntity = userRepository.getReferenceById(TestUtil.TestUser.userId);
        final UserEntity ignoredUserEntity = userRepository.getReferenceById(ignoredUserId);
        final IgnoredUserEntityRelation pk = new IgnoredUserEntityRelation(userEntity, ignoredUserEntity);
        assertThat(ignoredUserRepository.existsByIgnoredUserRelation(pk)).isTrue();

//                .andDo(document("user/ignore/POST",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("ignoredUserId").description("(필수) 무시하기 하려는 유저의 id 설정")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
//                        )
//                ));
    }

    @Test
    @DisplayName("[유저 무시 API] - 유저 무시하기 취소")
    void cancelIgnoreUser() throws Exception {
        //given
        final long ignoredUserId = 2L;
        Scenario.createUser().id(ignoredUserId).request()
                .advance().ignoreUser().request();

        //when
        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .delete("/v1/user/ignore/{ignoredUserId}", ignoredUserId)
                                .header(AUTHORIZATION_HEADER, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andDo(document("user/ignore/DELETE",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("ignoredUserId").description("(필수) 무시하기 했던 유저의 id 설정")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답")
//                        )
//                ));

    }

    @Test
    @DisplayName("[유저 무시 API] - 무시하기한 유저 목록 조회")
    void getIgnoreUser() throws Exception {
        //given
        Scenario.createUser().id(2L).request()
                .advance().ignoreUser().ignoredUserId(2L).request()
                .advance().createUser().id(3L).request()
                .advance().ignoreUser().ignoredUserId(3L).request()
                .advance().createUser().id(4L).request();
        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/user/ignore", 10)
                                .header(AUTHORIZATION_HEADER, accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        response.andExpect(jsonPath("$.data.length()").value(2));
//                .andDo(document("user/ignore/GET",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("무시한 유저 id"),
//                                fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("무시한 유저 닉네임"),
//                                fieldWithPath("data[].profileImgUrl").type(JsonFieldType.STRING).description("무시한 유저 프로필 이미지 링크")
//                        )
//                ));

    }
}