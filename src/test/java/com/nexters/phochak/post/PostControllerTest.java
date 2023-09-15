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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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

}
