package com.nexters.phochak.common;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

public class DocumentGenerator {
    public static void login(final ResultActions response) throws Exception {
        response.andDo(document("v2/auth/login",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("provider").description("(필수) OAuth 서비스 이름(ex. kakao, apple, naver)")
                ),
                queryParameters(
                        parameterWithName("token").description("(필수) token (Access token or Identify Token)"),
                        parameterWithName("fcmDeviceToken").description("(선택) FCM client 식별 토큰"),
                        parameterWithName("operatingSystem").description("(선택) 운영체제 (ANDROID, IOS)")
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

    public static void checkNickname(final ResultActions response) throws Exception {
        response.andDo(document("user/check/nickname",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("nickname").description("(필수) 중복확인하고자 하는 닉네임 ('#' 때문에 URL 인코딩 처리해주세요)")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data.isDuplicated").type(JsonFieldType.BOOLEAN).description("닉네임 중복여부")
                )
        ));
    }

    public static void modifyNickname(final ResultActions response) throws Exception {
        response.andDo(document("user/modify/nickname",
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

    public static void getOtherUserInfo(final ResultActions response) throws Exception {
        response.andDo(document("user",
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

    public static void getMyUserInfo(final ResultActions response) throws Exception {
        response.andDo(document("user/me",
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

    public static void ignoreUser(final ResultActions response) throws Exception {
        response.andDo(document("user/ignore/POST",
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

    public static void cancelIgnoreUser(final ResultActions response) throws Exception {
        response.andDo(document("user/ignore/DELETE",
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

    public static void getIgnoredUser(final ResultActions response) throws Exception {
        response.andDo(document("user/ignore/GET",
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

    public static void createPost(final ResultActions response) throws Exception {
        response.andDo(document("post/POST",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("category").description("카테고리 ex) TOUR / RESTAURANT / CAFE"),
                        fieldWithPath("uploadKey").description("발급 받았던 업로드 키"),
                        fieldWithPath("hashtags").description("해시태그 배열 ex) [\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                )
        ));
    }

    public static void updatePost(final ResultActions response) throws Exception {
        response.andDo(document("post/PUT",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("postId").description("(필수) 수정할 포스트의 id")
                ),
                requestFields(
                        fieldWithPath("category").description("카테고리 ex) TOUR / RESTAURANT / CAFE"),
                        fieldWithPath("hashtags").description("해시태그 배열 ex) [\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                )
        ));
    }

    public static void deletePost(final ResultActions response) throws Exception {
        response.andDo(document("post/DELETE",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("postId").description("(필수) 삭제할 포스트의 id")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("null")
                )
        ));
    }

    public static void getPostList_initial(final ResultActions response) throws Exception {
        response.andDo(document("post/list/initial",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
                        parameterWithName("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
                        parameterWithName("lastId").description("(선택) 초기 요청에서는 필요하지 않음").optional()
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("(필수) JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                        fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                        fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                        fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                        fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                        fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
                        fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                        fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                        fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
                        fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                        fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
                        fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
                )
        ));
    }

    public static void getPostList_after(final ResultActions response) throws Exception {
        response.andDo(document("post/list/initial",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
                        parameterWithName("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
                        parameterWithName("lastId").description("(필수) 마지막으로 받은 게시글 id")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("(필수) JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                        fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                        fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                        fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                        fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                        fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
                        fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                        fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                        fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
                        fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                        fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
                        fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
                )
        ));
    }

    public static void getPostList_last(final ResultActions response) throws Exception {
        response.andDo(document("post/list/initial",
                preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
                        parameterWithName("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
                        parameterWithName("lastId").description("(필수) 마지막으로 받은 게시글 id")
                ),
                requestHeaders(
                        headerWithName(AUTHORIZATION_HEADER)
                                .description("(필수) JWT Access Token")
                ),
                responseFields(
                        fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                        fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                        fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                        fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                        fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                        fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                        fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
                        fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                        fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                        fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
                        fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                        fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                        fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
                        fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
                        fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
                )
        ));
    }

    public static void hashtagAutocomplete(final ResultActions response) throws Exception {
        response.andDo(document("post/hashtag/autocomplete",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("hashtag").description("(필수) 검색할 해시태그").optional(),
                                parameterWithName("resultSize").description("(필수) 검색 결과 크기").optional()
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("자동완성 해시태그 리스트")
                        )
                ));
    }

    public static void updatePostView(final ResultActions response) throws Exception {
        response.andDo(document("post/view",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(필수) 게시글 id")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(필수) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 내용")
                        )
                ));
    }
}
