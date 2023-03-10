package com.nexters.phochak.controller;

import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.dto.PostFetchDto.PostShortsInformation;
import com.nexters.phochak.dto.PostFetchDto.PostUserInformation;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostFilter;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.PostSortOption;
import com.nexters.phochak.specification.ShortsStateEnum;
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

import java.util.List;

import static com.nexters.phochak.auth.aspect.AuthAspect.AUTHORIZATION_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
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
class PostControllerTest extends RestDocs {

    @Mock
    PostService postService;

    @InjectMocks
    PostController postController;

    MockMvc mockMvc;
    PostUserInformation user;
    PostShortsInformation shorts;
    PostPageResponseDto post1;
    PostPageResponseDto post2;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController).build();

        user = PostUserInformation.builder()
                .id(3)
                .nickname("testUser")
                .profileImgUrl("profileImage")
                .build();

        shorts = PostShortsInformation.builder()
                .id(1L)
                .state(ShortsStateEnum.OK)
                .thumbnailUrl("thumbnail url")
                .shortsUrl("shorts url")
                .build();

        List<String> hashtags1 = List.of("????????????1", "????????????2");
        List<String> hashtags2 = List.of("????????????2", "????????????3");

        post1 = PostPageResponseDto.builder()
                .id(5L)
                .user(user)
                .shorts(shorts)
                .view(5L)
                .category(PostCategoryEnum.RESTAURANT)
                .like(10)
                .isLiked(Boolean.TRUE)
                .hashtags(hashtags1)
                .build();

        post2 = PostPageResponseDto.builder()
                .id(7L)
                .user(user)
                .shorts(shorts)
                .view(12L)
                .category(PostCategoryEnum.TOUR)
                .like(21)
                .isLiked(Boolean.FALSE)
                .hashtags(hashtags2)
                .build();
    }

    @Test
    @DisplayName("????????? ????????? ???????????? ?????? API - ??????")
    void updatePostView() throws Exception {
        Long postId = 1L;

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .post("/v1/post/{postId}/view", postId)
                                .header(AUTHORIZATION_HEADER, "access token"))
                .andExpect(status().isOk())
                .andDo(document("post/view",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("(??????) ????????? id")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API - ??? ??????")
    void getPostList_initial() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .sortOption(PostSortOption.LATEST)
                .pageSize(2)
                .build();

        List<PostPageResponseDto> result = List.of(post2, post1);

        when(postService.getNextCursorPage(any(), any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortOption", customCursor.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                                .header(AUTHORIZATION_HEADER, "access token"))
                .andExpect(status().isOk())
                .andDo(document("post/list/initial",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("sortOption").description("(??????) ????????? ?????? ?????? (LIKE/LATEST/VIEW)"),
                                parameterWithName("pageSize").description("(??????) ????????? ??????(default: 5)").optional()
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("?????? shorts ????????? ??????(OK, FAIL, IN_PROGRESS)"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("????????? ???"),
                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("????????? ????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API - ?????? ??????")
    void getPostList_after() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(3)
                .sortOption(PostSortOption.LIKE)
                .lastId(20L)
                .sortValue(75)
                .build();

        List<String> hashtags = List.of("????????????4", "????????????5");

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(17L)
                .user(user)
                .shorts(shorts)
                .view(50L)
                .category(PostCategoryEnum.TOUR)
                .like(75)
                .isLiked(Boolean.TRUE)
                .hashtags(hashtags)
                .build();

        List<PostPageResponseDto> result = List.of(post3, post2, post1);

        when(postService.getNextCursorPage(any(), any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortValue", String.valueOf(customCursor.getSortValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("sortOption", customCursor.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                                .header(AUTHORIZATION_HEADER, "access token")
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/after",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("sortOption").description("(??????) ????????? ?????? ?????? (LIKE/LATEST/VIEW)"),
                                parameterWithName("sortValue").description("(sortOption??? LATEST??? ????????? ???????????? ??????) ??????????????? ?????? ???????????? ????????? ???????????? ?????? ?????? ???(LIKE??? ????????? ???, VIEW??? ?????????)"),
                                parameterWithName("lastId").description("(??????) ??????????????? ?????? ????????? id"),
                                parameterWithName("pageSize").description("(??????) ????????? ??????(default: 5)").optional()
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("?????? shorts ????????? ??????(OK, FAIL, IN_PROGRESS)"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("????????? ???"),
                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("????????? ????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API - ????????? ??????")
    void getPostList_last() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(5)
                .sortOption(PostSortOption.VIEW)
                .lastId(3L)
                .sortValue(100)
                .build();

        List<String> hashtags = List.of("????????????4", "????????????5");

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(20L)
                .user(user)
                .shorts(shorts)
                .view(63)
                .category(PostCategoryEnum.RESTAURANT)
                .like(28)
                .isLiked(Boolean.TRUE)
                .hashtags(hashtags)
                .build();

        List<PostPageResponseDto> result = List.of(post3, post2, post1);

        when(postService.getNextCursorPage(any(), any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortValue", String.valueOf(customCursor.getSortValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("sortOption", customCursor.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                                .header(AUTHORIZATION_HEADER, "access token")
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/last",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("sortOption").description("(??????) ????????? ?????? ?????? (LIKE/LATEST/VIEW)"),
                                parameterWithName("sortValue").description("(sortOption??? LATEST??? ????????? ???????????? ??????) ??????????????? ?????? ???????????? ????????? ???????????? ?????? ?????? ???(LIKE??? ????????? ???, VIEW??? ?????????)"),
                                parameterWithName("lastId").description("(??????) ??????????????? ?????? ????????? id"),
                                parameterWithName("pageSize").description("(??????) ????????? ??????(default: 5)").optional()
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("?????? shorts ????????? ??????(OK, FAIL, IN_PROGRESS)"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("????????? ???"),
                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("????????? ????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API - ?????? ???????????? ??????")
    void getPostList_uploaded() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(3)
                .sortOption(PostSortOption.LIKE)
                .lastId(20L)
                .sortValue(75)
                .build();

        PostFilter postFilter = PostFilter.UPLOADED;

        List<String> hashtags = List.of("??????", "?????????");

        PostUserInformation newUser = PostUserInformation.builder()
                .id(4L)
                .nickname("newUser")
                .profileImgUrl("profileImage")
                .build();

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(20L)
                .user(newUser)
                .shorts(shorts)
                .view(1000)
                .category(PostCategoryEnum.CAFE)
                .like(120)
                .isLiked(Boolean.TRUE)
                .hashtags(hashtags)
                .build();

        List<PostPageResponseDto> result = List.of(post3);


        when(postService.getNextCursorPage(any(), any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortValue", String.valueOf(customCursor.getSortValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("sortOption", customCursor.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                                .param("filter", postFilter.name())
                                .header(AUTHORIZATION_HEADER, "access token")
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/uploaded",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("sortOption").description("(??????) ????????? ?????? ?????? (LIKE/LATEST/VIEW)"),
                                parameterWithName("sortValue").description("(sortOption??? LATEST??? ????????? ???????????? ??????) ??????????????? ?????? ???????????? ????????? ???????????? ?????? ?????? ???(LIKE??? ????????? ???, VIEW??? ?????????)"),
                                parameterWithName("lastId").description("(??????) ??????????????? ?????? ????????? id"),
                                parameterWithName("pageSize").description("(??????) ????????? ??????(default: 5)").optional(),
                                parameterWithName("filter").description("(??????) ??????????????? ?????? ?????? (UPLOADED: ?????? ???????????? ?????????/LIKED: ?????? ???????????? ?????????)")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("?????? shorts ????????? ??????(OK, FAIL, IN_PROGRESS)"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("????????? ???"),
                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("????????? ????????? ????????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????? API - ?????? ???????????? ??????")
    void getPostList_liked() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(3)
                .sortOption(PostSortOption.LIKE)
                .lastId(20L)
                .sortValue(75)
                .build();

        PostFilter postFilter = PostFilter.LIKED;

        List<String> hashtags = List.of("????????????", "?????????");

        PostUserInformation newUser = PostUserInformation.builder()
                .id(4L)
                .nickname("newUser")
                .profileImgUrl("profileImage")
                .build();

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(20L)
                .user(newUser)
                .shorts(shorts)
                .view(1000)
                .category(PostCategoryEnum.CAFE)
                .like(120)
                .isLiked(Boolean.TRUE)
                .hashtags(hashtags)
                .build();

        List<PostPageResponseDto> result = List.of(post3, post1);


        when(postService.getNextCursorPage(any(), any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortValue", String.valueOf(customCursor.getSortValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("sortOption", customCursor.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                                .param("filter", postFilter.name())
                                .header(AUTHORIZATION_HEADER, "access token")
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/liked",
                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("sortOption").description("(??????) ????????? ?????? ?????? (LIKE/LATEST/VIEW)"),
                                parameterWithName("sortValue").description("(sortOption??? LATEST??? ????????? ???????????? ??????) ??????????????? ?????? ???????????? ????????? ???????????? ?????? ?????? ???(LIKE??? ????????? ???, VIEW??? ?????????)"),
                                parameterWithName("lastId").description("(??????) ??????????????? ?????? ????????? id"),
                                parameterWithName("pageSize").description("(??????) ????????? ??????(default: 5)").optional(),
                                parameterWithName("filter").description("(??????) ??????????????? ?????? ?????? (UPLOADED: ?????? ???????????? ?????????/LIKED: ?????? ???????????? ?????????)")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER)
                                        .description("(??????) JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("????????? id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("?????? ?????????"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("data[].shorts.state").type(JsonFieldType.STRING).description("?????? shorts ????????? ??????(OK, FAIL, IN_PROGRESS)"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ??????"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("data[].hashtags").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("????????? ????????????"),
                                fieldWithPath("data[].like").type(JsonFieldType.NUMBER).description("????????? ???"),
                                fieldWithPath("data[].isLiked").type(JsonFieldType.BOOLEAN).description("????????? ????????? ????????? ??????")
                        )
                ));
    }
}
