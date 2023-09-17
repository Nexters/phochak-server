//package com.nexters.phochak.deprecated.controller;
//
//import com.nexters.phochak.common.docs.RestDocs;
//import com.nexters.phochak.post.adapter.in.web.PostController;
//import com.nexters.phochak.post.adapter.out.persistence.PostFilter;
//import com.nexters.phochak.post.adapter.out.persistence.PostSortOption;
//import com.nexters.phochak.post.application.port.in.CustomCursorDto;
//import com.nexters.phochak.post.application.port.in.PostFetchDto.PostShortsInformation;
//import com.nexters.phochak.post.application.port.in.PostFetchDto.PostUserInformation;
//import com.nexters.phochak.post.application.port.in.PostPageResponseDto;
//import com.nexters.phochak.post.application.port.in.PostUseCase;
//import com.nexters.phochak.post.domain.PostCategoryEnum;
//import com.nexters.phochak.shorts.domain.ShortsStateEnum;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.restdocs.RestDocumentationContextProvider;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Disabled
//@ExtendWith(MockitoExtension.class)
//class PostControllerTest extends RestDocs {
//
//    @Mock
//    PostUseCase postUseCase;
//
//    @InjectMocks
//    PostController postController;
//
//    MockMvc mockMvc;
//    PostUserInformation user;
//    PostShortsInformation shorts;
//    PostPageResponseDto post1;
//    PostPageResponseDto post2;
//
//    @BeforeEach
//    void setUp(RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = getMockMvcBuilder(restDocumentation, postController).build();
//
//        user = PostUserInformation.builder()
//                .id(3)
//                .nickname("testUser")
//                .profileImgUrl("profileImage")
//                .build();
//
//        shorts = PostShortsInformation.builder()
//                .id(1L)
//                .state(ShortsStateEnum.OK)
//                .thumbnailUrl("thumbnail url")
//                .shortsUrl("shorts url")
//                .build();
//
//        List<String> hashtags1 = List.of("해시태그1", "해시태그2");
//        List<String> hashtags2 = List.of("해시태그2", "해시태그3");
//
//        post1 = PostPageResponseDto.builder()
//                .id(5L)
//                .user(user)
//                .shorts(shorts)
//                .view(5L)
//                .category(PostCategoryEnum.RESTAURANT)
//                .like(10)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags1)
//                .isBlind(Boolean.TRUE)
//                .build();
//
//        post2 = PostPageResponseDto.builder()
//                .id(7L)
//                .user(user)
//                .shorts(shorts)
//                .view(12L)
//                .category(PostCategoryEnum.TOUR)
//                .like(21)
//                .isLiked(Boolean.FALSE)
//                .hashtags(hashtags2)
//                .isBlind(Boolean.FALSE)
//                .build();
//    }
//
//    @Test
//    @DisplayName("포스트 조회수 업데이트 요청 API - 성공")
//    void updatePostView() throws Exception {
//        Long postId = 1L;
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .post("/v1/post/{postId}/view", postId)
//                                .header(AUTHORIZATION_HEADER, "access token"))
//                .andExpect(status().isOk())
//                .andDo(document("post/view",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("postId").description("(필수) 게시글 id")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 내용")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 첫 요청")
//    void getPostList_initial() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .sortOption(PostSortOption.LATEST)
//                .filter(PostFilter.NONE)
//                .pageSize(2)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post2, post1);
//
//        when(postUseCase.getNextCursorPage(any())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list")
//                                .param("sortOption", customCursorDto.getSortOption().name())
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .header(AUTHORIZATION_HEADER, "access token"))
//                .andExpect(status().isOk())
//                .andDo(document("post/list/initial",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional()
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 이후 요청")
//    void getPostList_after() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .pageSize(3)
//                .sortOption(PostSortOption.LIKE)
//                .filter(PostFilter.NONE)
//                .lastId(20L)
//                .sortValue(75)
//                .build();
//
//        List<String> hashtags = List.of("해시태그4", "해시태그5");
//
//        PostPageResponseDto post3 = PostPageResponseDto.builder()
//                .id(17L)
//                .user(user)
//                .shorts(shorts)
//                .view(50L)
//                .category(PostCategoryEnum.TOUR)
//                .like(75)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags)
//                .isBlind(Boolean.FALSE)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post3, post2, post1);
//
//        when(postUseCase.getNextCursorPage(any())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list")
//                                .param("sortValue", String.valueOf(customCursorDto.getSortValue()))
//                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
//                                .param("sortOption", customCursorDto.getSortOption().name())
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/list/after",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
//                                fieldWithPath("sortValue").description("(sortOption이 LATEST인 경우를 제외하고 필수) 마지막으로 받은 페이지의 마지막 게시글의 정렬 기준 값(LIKE면 좋아요 수, VIEW면 조회수)"),
//                                fieldWithPath("lastId").description("(필수) 마지막으로 받은 게시글 id"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional()
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 마지막 요청")
//    void getPostList_last() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .pageSize(5)
//                .sortOption(PostSortOption.VIEW)
//                .filter(PostFilter.NONE)
//                .lastId(3L)
//                .sortValue(100)
//                .build();
//
//        List<String> hashtags = List.of("해시태그4", "해시태그5");
//
//        PostPageResponseDto post3 = PostPageResponseDto.builder()
//                .id(20L)
//                .user(user)
//                .shorts(shorts)
//                .view(63)
//                .category(PostCategoryEnum.RESTAURANT)
//                .like(28)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags)
//                .isBlind(Boolean.FALSE)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post3, post2, post1);
//
//        when(postUseCase.getNextCursorPage(any())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list")
//                                .param("sortValue", String.valueOf(customCursorDto.getSortValue()))
//                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
//                                .param("sortOption", customCursorDto.getSortOption().name())
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/list/last",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
//                                fieldWithPath("sortValue").description("(sortOption이 LATEST인 경우를 제외하고 필수) 마지막으로 받은 페이지의 마지막 게시글의 정렬 기준 값(LIKE면 좋아요 수, VIEW면 조회수)"),
//                                fieldWithPath("lastId").description("(필수) 마지막으로 받은 게시글 id"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional()
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 나 또는 다른 유저가 업로드한 영상")
//    void getPostList_uploaded() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .pageSize(3)
//                .sortOption(PostSortOption.LIKE)
//                .filter(PostFilter.UPLOADED)
//                .targetUserId(10L)
//                .lastId(20L)
//                .sortValue(75)
//                .build();
//
//        List<String> hashtags = List.of("누군가가", "업로드");
//
//        PostUserInformation newUser = PostUserInformation.builder()
//                .id(4L)
//                .nickname("newUser")
//                .profileImgUrl("profileImage")
//                .build();
//
//        PostPageResponseDto post3 = PostPageResponseDto.builder()
//                .id(20L)
//                .user(newUser)
//                .shorts(shorts)
//                .view(1000)
//                .category(PostCategoryEnum.CAFE)
//                .like(120)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags)
//                .isBlind(Boolean.FALSE)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post3);
//
//
//        when(postUseCase.getNextCursorPage(any())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list")
//                                .param("sortValue", String.valueOf(customCursorDto.getSortValue()))
//                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
//                                .param("sortOption", customCursorDto.getSortOption().name())
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .param("filter", customCursorDto.getFilter().name())
//                                .param("targetUserId", String.valueOf(customCursorDto.getTargetUserId()))
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/list/uploaded",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
//                                fieldWithPath("sortValue").description("(sortOption이 LATEST인 경우를 제외하고 필수) 마지막으로 받은 페이지의 마지막 게시글의 정렬 기준 값(LIKE면 좋아요 수, VIEW면 조회수)"),
//                                fieldWithPath("lastId").description("(필수) 마지막으로 받은 게시글 id"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
//                                fieldWithPath("filter").description("(선택) 마이페이지 필터 조건 (UPLOADED: 내가 업로드한 동영상/LIKED: 내가 좋아요한 동영상)"),
//                                fieldWithPath("targetUserId").description("(선택) 조회할 페이지의 유저 id (본인의 페이지라면 생략 가능)")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 내가 좋아요한 영상")
//    void getPostList_liked() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .pageSize(3)
//                .sortOption(PostSortOption.LIKE)
//                .filter(PostFilter.LIKED)
//                .lastId(20L)
//                .sortValue(75)
//                .build();
//
//        List<String> hashtags = List.of("좋아요한", "게시글");
//
//        PostUserInformation newUser = PostUserInformation.builder()
//                .id(4L)
//                .nickname("newUser")
//                .profileImgUrl("profileImage")
//                .build();
//
//        PostPageResponseDto post3 = PostPageResponseDto.builder()
//                .id(20L)
//                .user(newUser)
//                .shorts(shorts)
//                .view(1000)
//                .category(PostCategoryEnum.CAFE)
//                .like(120)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags)
//                .isBlind(Boolean.FALSE)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post3, post1);
//
//
//        when(postUseCase.getNextCursorPage(any())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list")
//                                .param("sortValue", String.valueOf(customCursorDto.getSortValue()))
//                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
//                                .param("sortOption", customCursorDto.getSortOption().name())
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .param("filter", customCursorDto.getFilter().name())
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/list/liked",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("sortOption").description("(필수) 게시글 정렬 기준 (LIKE/LATEST/VIEW)"),
//                                fieldWithPath("sortValue").description("(sortOption이 LATEST인 경우를 제외하고 필수) 마지막으로 받은 페이지의 마지막 게시글의 정렬 기준 값(LIKE면 좋아요 수, VIEW면 조회수)"),
//                                fieldWithPath("lastId").description("(필수) 마지막으로 받은 게시글 id"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
//                                fieldWithPath("filter").description("(선택) 마이페이지 필터 조건 (UPLOADED: 내가 업로드한 동영상/LIKED: 내가 좋아요한 동영상)")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 목록 조회 API - 해시태그 검색")
//    void getPostList_searched() throws Exception {
//        CustomCursorDto customCursorDto = CustomCursorDto.builder()
//                .pageSize(3)
//                .lastId(20L)
//                .category(PostCategoryEnum.CAFE)
//                .build();
//
//        List<String> hashtags = List.of("해시태그1", "해시태그2");
//
//        PostUserInformation newUser = PostUserInformation.builder()
//                .id(4L)
//                .nickname("newUser")
//                .profileImgUrl("profileImage")
//                .build();
//
//        PostPageResponseDto post3 = PostPageResponseDto.builder()
//                .id(20L)
//                .user(newUser)
//                .shorts(shorts)
//                .view(1000)
//                .category(PostCategoryEnum.CAFE)
//                .like(120)
//                .isLiked(Boolean.TRUE)
//                .hashtags(hashtags)
//                .isBlind(Boolean.FALSE)
//                .build();
//
//        List<PostPageResponseDto> result = List.of(post3, post1);
//
//
//        when(postUseCase.getNextCursorPage(any(), anyString())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/list/search")
//                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
//                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
//                                .param("hashtag", String.valueOf(hashtags.get(1)))
//                                .param("category", String.valueOf(customCursorDto.getCategory()))
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/list/search",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("lastId").description("(선택) 마지막으로 받은 게시글 id"),
//                                fieldWithPath("pageSize").description("(선택) 페이지 크기(default: 5)").optional(),
//                                fieldWithPath("hashtag").description("(선택) 검색할 해시태그").optional(),
//                                fieldWithPath("category").description("(선택) 게시글 카테고리 ex) CAFE").optional()
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
//                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
//                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
//                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
//                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
//                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
//                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("현재 shorts 인코딩 상태(OK, FAIL, IN_PROGRESS)"),
//                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
//                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
//                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
//                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
//                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
//                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("좋아요 수"),
//                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("조회한 유저의 좋아요 여부"),
//                                fieldWithPath("data[].isBlind").type(JsonFieldType.BOOLEAN).description("해당 게시글의 신고 누적 여부")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("포스트 API - 해시태그 자동완성")
//    void hastagAutocomplete() throws Exception {
//        String hashtag = "해시";
//        int resultSize = 3;
//        List<String> result = List.of("해시", "해시태", "해시태그123");
//
//        when(postUseCase.getHashtagAutocomplete(anyString(), anyInt())).thenReturn(result);
//
//        mockMvc.perform(
//                        RestDocumentationRequestBuilders
//                                .get("/v1/post/hashtag/autocomplete")
//                                .param("hashtag", hashtag)
//                                .param("resultSize", String.valueOf(resultSize))
//                                .header(AUTHORIZATION_HEADER, "access token")
//                )
//                .andExpect(status().isOk())
//                .andDo(document("post/hashtag/autocomplete",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("hashtag").description("(필수) 검색할 해시태그").optional(),
//                                fieldWithPath("resultSize").description("(필수) 검색 결과 크기").optional()
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("자동완성 해시태그 리스트")
//                        )
//                ));
//    }
//}
