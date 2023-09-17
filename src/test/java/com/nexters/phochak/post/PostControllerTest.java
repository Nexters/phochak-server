package com.nexters.phochak.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.DocumentGenerator;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.adapter.in.web.PostController;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.application.port.in.PostUpdateRequestDto;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends RestDocsApiTest {
    @Autowired
    PostController postController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    ShortsRepository shortsRepository;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController).build();
        TestUtil.setMockMvc(mockMvc);
    }

    @Test
    @DisplayName("포스트 API - 게시글 생성 성공")
    void createPost() throws Exception {
        //given
        final ResultActions response = Scenario.createPost().request().getResponse();

        Assertions.assertAll(
                () -> assertThat(postRepository.count()).isEqualTo(1L),
                () -> assertThat(shortsRepository.count()).isEqualTo(1L),
                () -> assertThat(hashtagRepository.count()).isEqualTo(3L));

        DocumentGenerator.createPost(response);
    }

    @Test
    @DisplayName("포스트 API - 게시글 수정 성공")
    void updatePost() throws Exception {
        //given
        Scenario.createPost().hashtagList(List.of("태그1")).postCategoryEnum(PostCategoryEnum.CAFE).request();
        final long postId = 1;
        PostUpdateRequestDto request = new PostUpdateRequestDto(
                List.of("수정1", "수정2"),
                PostCategoryEnum.RESTAURANT.name()
        );

        //when
        final ResultActions response = mockMvc.perform(put("/v1/post/{postId}", postId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));

        //then
        Assertions.assertAll(
                () -> assertThat(hashtagRepository.existsByTag("태그1")).isFalse(),
                () -> assertThat(hashtagRepository.existsByTag("수정1")).isTrue(),
                () -> assertThat(hashtagRepository.existsByTag("수정2")).isTrue());

        //doc
        DocumentGenerator.updatePost(response);
    }

    @Test
    @DisplayName("포스트 API - 게시글 삭제 성공")
    void deletePost_success() throws Exception {
        //given
        Scenario.createPost().hashtagList(List.of("태그1")).postCategoryEnum(PostCategoryEnum.CAFE).request();
        final long postId = 1;

        //when
        final ResultActions response = mockMvc.perform(delete("/v1/post/{postId}", postId)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));

        //then
        Assertions.assertAll(
                () -> assertThat(postRepository.count()).isZero(),
                () -> assertThat(shortsRepository.count()).isZero(),
                () -> assertThat(hashtagRepository.count()).isZero());

        //doc
        DocumentGenerator.deletePost(response);
    }

    @Test
    @DisplayName("포스트 API - 해시태그 자동완성")
    void hashtagAutocomplete() throws Exception {
        //given
        Scenario.createPost().uploadKey("post1").hashtagList(List.of("해시", "해시게시글하나에종복")).postCategoryEnum(PostCategoryEnum.CAFE).request().advance().
        createPost().uploadKey("post2").hashtagList(List.of("해시태", "tag")).postCategoryEnum(PostCategoryEnum.CAFE).request().advance().
        createPost().uploadKey("post3").hashtagList(List.of("해시태그123", "tagtag")).postCategoryEnum(PostCategoryEnum.CAFE).request().advance().
        createPost().uploadKey("post4").hashtagList(List.of("없음")).postCategoryEnum(PostCategoryEnum.CAFE).request().advance();

        String hashtag = "해시";
        int resultSize = 3;

        //when
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/hashtag/autocomplete")
                                .param("hashtag", hashtag)
                                .param("resultSize", String.valueOf(resultSize))
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken)
                )
                .andExpect(status().isOk());

        //then
        response.andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(3));

        //doc
        DocumentGenerator.hashtagAutocomplete(response);
    }


    @Test
    @DisplayName("포스트 조회수 업데이트 요청 API - 성공")
    void updatePostView() throws Exception {
        //given
        Long postId = 1L;
        Scenario.createPost().request();

        //when, then
        final ResultActions response = mockMvc.perform(
                        post("/v1/post/{postId}/view", postId)
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk());

        //doc
        DocumentGenerator.updatePostView(response);
    }

    @Test
    @DisplayName("포착하기 요청 API - 포착하기 성공")
    void addPhochak() throws Exception {
        Scenario.createPost().request();
        final Long postId = 1L;

        //when, then
        mockMvc.perform(post("/v1/post/{postId}/likes/", postId).header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));
//                .andDo(document("post/{postId}/likes/post",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("postId").description("(필수) 해당 게시글 Id")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
//                        )
//                ));
    }

    @Test
    @DisplayName("포착 취소하기 요청 API - 포착 취소하기 성공")
    void cancelPhochak() throws Exception {
        Scenario.createPost().request();
        final Long postId = 1L;

        //when, then
        mockMvc.perform(delete("/v1/post/{postId}/likes/", postId).header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));
//                .andDo(document("post/{postId}/likes/delete",
//                        preprocessRequest(modifyUris().scheme("http").host("101.101.209.228").removePort(), prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("postId").description("(필수) 해당 게시글 Id")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("(필수) JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
//                        )
//                ));

    }
}
