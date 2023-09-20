package com.nexters.phochak.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.DocumentGenerator;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.adapter.in.web.PostController;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostFilter;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.shorts.adapter.in.web.ShortsController;
import com.nexters.phochak.shorts.domain.EncodingStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

class PostControllerPagingTest extends RestDocsApiTest {
    @Autowired
    PostController postController;
    @Autowired
    ShortsController shortsController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController, shortsController).build();
        TestUtil.setMockMvc(mockMvc);
    }

    @Test
    @DisplayName("포스트 목록 조회 API - 첫 요청")
    void getPostList_initial() throws Exception {
        //given, when
        final ResultActions response = Scenario.createPost().uploadKey("test1").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test1").request().advance()
                .createPost().uploadKey("test2").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test2").request().advance()
                .createPost().uploadKey("test3").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test3").request().advance()
                .createPost().uploadKey("test4").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test4").request().advance()
                .createPost().uploadKey("test5").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test5").request().advance()
                .getPostList().pageSize(2).request().getResponse();

        //then
        response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(2));

        //doc
        DocumentGenerator.getPostList_initial(response);
    }

        @Test
    @DisplayName("포스트 목록 조회 API - 이후 요청")
    void getPostList_after() throws Exception {
            //given, when
            final ResultActions response = Scenario.createPost().uploadKey("test1").request().advance()
                    .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test1").request().advance()
                    .createPost().uploadKey("test2").request().advance()
                    .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test2").request().advance()
                    .createPost().uploadKey("test3").request().advance()
                    .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test3").request().advance()
                    .createPost().uploadKey("test4").request().advance()
                    .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test4").request().advance()
                    .createPost().uploadKey("test5").request().advance()
                    .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test5").request().advance()
                    .getPostList().lastId(4).pageSize(2).request().getResponse();

            //then
            response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(2));

            //doc
            DocumentGenerator.getPostList_after(response);
    }

    @Test
    @DisplayName("포스트 목록 조회 API - 마지막 요청")
    void getPostList_last() throws Exception {
        //given, when
        final ResultActions response = Scenario.createPost().uploadKey("test1").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test1").request().advance()
                .createPost().uploadKey("test2").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test2").request().advance()
                .createPost().uploadKey("test3").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test3").request().advance()
                .createPost().uploadKey("test4").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test4").request().advance()
                .createPost().uploadKey("test5").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test5").request().advance()
                .getPostList().lastId(2).pageSize(2).request().getResponse();

        //then
        response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(1));

        //doc
        DocumentGenerator.getPostList_last(response);
    }
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
//        PostFetchDto.PostUserInformation newUser = PostFetchDto.PostUserInformation.builder()
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
    @Test
    @DisplayName("포스트 목록 조회 API - 내가 좋아요한 영상")
    void getPostList_liked() throws Exception {
        //given, when
        final ResultActions response = Scenario.createPost().uploadKey("test1").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test1").request().advance()
                .addPhochak().postId(1L).request().advance()
                .createPost().uploadKey("test2").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test2").request().advance()
                .addPhochak().postId(2L).request().advance()
                .createPost().uploadKey("test3").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test3").request().advance()
                .addPhochak().postId(3L).request().advance()
                .createPost().uploadKey("test4").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test4").request().advance()
                .createPost().uploadKey("test5").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test5").request().advance()
                .getPostList().postFilter(PostFilter.LIKED).pageSize(5).request().getResponse();

        //then
        response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(3));

        //docs
        DocumentGenerator.getPostList_liked(response);
    }


    @Test
    @DisplayName("포스트 목록 조회 API - 해시태그 검색")
    void getPostList_searched() throws Exception {

        //given, when
        final ResultActions response = Scenario.createPost().hashtagList(List.of("태그")).uploadKey("test1").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test1").request().advance()
                .createPost().hashtagList(List.of("태그")).uploadKey("test2").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test2").request().advance()
                .createPost().hashtagList(List.of("태그")).uploadKey("test3").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test3").request().advance()
                .createPost().uploadKey("test4").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test4").request().advance()
                .createPost().uploadKey("test5").request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).filePathByUploadKey("test5").request().advance()
                .getPostList().hashtag("태그").postFilter(PostFilter.SEARCH).pageSize(5).request().getResponse();

        //then
        response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(3));

        //docs
        DocumentGenerator.getPostList_searched(response);
    }

}