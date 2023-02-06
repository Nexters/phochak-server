package com.nexters.phochak.controller;

import com.nexters.phochak.docs.RestDocs;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.PostSortCriteria;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest extends RestDocs {

    @Mock
    PostService postService;

    @InjectMocks
    PostController postController;

    MockMvc mockMvc;

    User user;
    Shorts shorts;
    PostPageResponseDto post1;
    PostPageResponseDto post2;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController).build();
        user = User.builder()
                .id(3L)
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("123456789")
                .nickname("testUser")
                .profileImgUrl("profileImage")
                .build();

        shorts = Shorts.builder()
                .id(1L)
                .thumbnailUrl("thumbnail url")
                .shortsUrl("shorts url")
                .build();

        post1 = PostPageResponseDto.builder()
                .id(5L)
                .user(user)
                .shorts(shorts)
                .view(5L)
                .postCategoryEnum(PostCategoryEnum.RESTAURANT)
                .phochakCount(10L)
                .build();

        post2 = PostPageResponseDto.builder()
                .id(7L)
                .user(user)
                .shorts(shorts)
                .view(12L)
                .postCategoryEnum(PostCategoryEnum.TOUR)
                .phochakCount(21L)
                .build();
    }

    @Test
    @DisplayName("포스트 목록 조회 API - 첫 요청")
    void getPostList_initial() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .isInitialRequest(true)
                .postSortCriteria(PostSortCriteria.LATEST)
                .pageSize(2)
                .build();

        List<PostPageResponseDto> result = List.of(post2, post1);

        when(postService.getNextCursorPage(any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("isInitialRequest", String.valueOf(customCursor.getIsInitialRequest()))
                                .param("postSortCriteria", customCursor.getPostSortCriteria().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/initial",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("isInitialRequest").description("첫 요청 여부 (true/false)"),
                                parameterWithName("postSortCriteria").description("게시글 정렬 기준 (PHOCHAK/LATEST/VIEW)"),
                                parameterWithName("pageSize").description("페이지 크기(default: 5)")
                        ),
                        responseFields(
                                fieldWithPath("resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("data[].postCategoryEnum").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].phochakCount").type(JsonFieldType.NUMBER).description("포착(좋아요) 수")
                        )
                ));
    }

    @Test
    @DisplayName("포스트 목록 조회 API - 이후 요청")
    void getPostList_after() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(3)
                .isInitialRequest(false)
                .postSortCriteria(PostSortCriteria.PHOCHAK)
                .lastId(3L)
                .lastCriteriaValue(75)
                .build();

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(17L)
                .user(user)
                .shorts(shorts)
                .view(50L)
                .postCategoryEnum(PostCategoryEnum.TOUR)
                .phochakCount(75L)
                .build();

        List<PostPageResponseDto> result = List.of(post3, post2, post1);

        when(postService.getNextCursorPage(any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("isInitialRequest", String.valueOf(customCursor.getIsInitialRequest()))
                                .param("lastCriteriaValue", String.valueOf(customCursor.getLastCriteriaValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("postSortCriteria", customCursor.getPostSortCriteria().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/after",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("isInitialRequest").description("첫 요청 여부 (true/false)"),
                                parameterWithName("postSortCriteria").description("게시글 정렬 기준 (PHOCHAK/LATEST/VIEW)"),
                                parameterWithName("lastCriteriaValue").description("마지막으로 받은 정렬 기준 값(작거나 같은 값만 페이지에 포함), LATEST의 경우에는 nullable"),
                                parameterWithName("lastId").description("마지막으로 받은 게시글 id(크거나 같은 id의 게시글만 페이지에 포함)"),
                                parameterWithName("pageSize").description("페이지 크기(default: 5)")
                        ),
                        responseFields(
                                fieldWithPath("resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("data[].postCategoryEnum").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].phochakCount").type(JsonFieldType.NUMBER).description("포착(좋아요) 수")
                        )
                ));
    }

    @Test
    @DisplayName("포스트 목록 조회 API - 마지막 요청")
    void getPostList_last() throws Exception {
        CustomCursor customCursor = CustomCursor.builder()
                .pageSize(5)
                .isInitialRequest(false)
                .postSortCriteria(PostSortCriteria.VIEW)
                .lastId(3L)
                .lastCriteriaValue(100)
                .build();

        PostPageResponseDto post3 = PostPageResponseDto.builder()
                .id(20L)
                .user(user)
                .shorts(shorts)
                .view(63)
                .postCategoryEnum(PostCategoryEnum.RESTAURANT)
                .phochakCount(28)
                .build();

        List<PostPageResponseDto> result = List.of(post3, post2, post1);

        when(postService.getNextCursorPage(any())).thenReturn(result);

        mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("isInitialRequest", String.valueOf(customCursor.getIsInitialRequest()))
                                .param("lastCriteriaValue", String.valueOf(customCursor.getLastCriteriaValue()))
                                .param("lastId", String.valueOf(customCursor.getLastId()))
                                .param("postSortCriteria", customCursor.getPostSortCriteria().name())
                                .param("pageSize", String.valueOf(customCursor.getPageSize()))
                )
                .andExpect(status().isOk())
                .andDo(document("post/list/last",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("isInitialRequest").description("첫 요청 여부 (true/false)"),
                                parameterWithName("postSortCriteria").description("게시글 정렬 기준 (PHOCHAK/LATEST/VIEW)"),
                                parameterWithName("lastCriteriaValue").description("마지막으로 받은 정렬 기준 값(작거나 같은 값만 페이지에 포함), LATEST의 경우에는 nullable"),
                                parameterWithName("lastId").description("마지막으로 받은 게시글 id(크거나 같은 id의 게시글만 페이지에 포함)"),
                                parameterWithName("pageSize").description("페이지 크기(default: 5)")
                        ),
                        responseFields(
                                fieldWithPath("resCode").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 id"),
                                fieldWithPath("data[].user.id").type(JsonFieldType.NUMBER).description("유저 id"),
                                fieldWithPath("data[].user.nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("data[].user.profileImgUrl").type(JsonFieldType.STRING).description("유저 프로필 이미지 링크"),
                                fieldWithPath("data[].shorts.id").type(JsonFieldType.NUMBER).description("영상 id"),
                                fieldWithPath("data[].shorts.thumbnailUrl").type(JsonFieldType.STRING).description("영상 썸네일 이미지 링크"),
                                fieldWithPath("data[].shorts.shortsUrl").type(JsonFieldType.STRING).description("영상 링크"),
                                fieldWithPath("data[].view").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("data[].postCategoryEnum").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].phochakCount").type(JsonFieldType.NUMBER).description("포착(좋아요) 수")
                        )
                ));
    }
}
