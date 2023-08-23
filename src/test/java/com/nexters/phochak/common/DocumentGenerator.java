package com.nexters.phochak.common;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class DocumentGenerator {
    public static void loginDocument(final ResultActions response) throws Exception {
        response.andDo(document("v2/auth/login",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("provider").description("(필수) OAuth 서비스 이름(ex. kakao, apple, naver)")
                ),
                queryParameters(
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
