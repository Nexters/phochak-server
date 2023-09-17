package com.nexters.phochak.shorts.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.adapter.in.web.PostController;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.shorts.EncodingStatusEnum;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

class ShortsControllerTest extends RestDocsApiTest {
    @Autowired
    ShortsController shortsController;
    @Autowired
    PostController postController;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PostRepository postRepository;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, shortsController, postController).build();
        TestUtil.setMockMvc(mockMvc);
    }

    @Test
    @Transactional(readOnly = true)
    @DisplayName("쇼츠 API - 인코딩 상태 WAITING")
    void encodingCallback_WAITING() throws Exception {
        //given, when
        Scenario.createPost().request().advance()
                .encodingCallback().status(EncodingStatusEnum.WAITING).request().advance();

        //then
        final PostEntity postEntity = postRepository.findById(1L).get();
        assertThat(postEntity.getShorts().getShortsStateEnum())
                .isEqualTo(ShortsStateEnum.IN_PROGRESS);
    }

    @Test
    @Transactional(readOnly = true)
    @DisplayName("쇼츠 API - 인코딩 상태 RUNNING")
    void encodingCallback_RUNNING() throws Exception {
        //given, when
        Scenario.createPost().request().advance()
                .encodingCallback().status(EncodingStatusEnum.WAITING).request().advance()
                .encodingCallback().status(EncodingStatusEnum.RUNNING).request().advance();
        //then
        final PostEntity postEntity = postRepository.findById(1L).get();
        assertThat(postEntity.getShorts().getShortsStateEnum())
                .isEqualTo(ShortsStateEnum.IN_PROGRESS);
    }

    @Test
    @Transactional(readOnly = true)
    @DisplayName("쇼츠 API - 인코딩 상태 COMPLETE")
    void encodingCallback_COMPLETE() throws Exception {
        //given, when
        Scenario.createPost().request().advance()
                .encodingCallback().status(EncodingStatusEnum.WAITING).request().advance()
                .encodingCallback().status(EncodingStatusEnum.RUNNING).request().advance()
                .encodingCallback().status(EncodingStatusEnum.COMPLETE).request().advance();
        //then
        final PostEntity postEntity = postRepository.findById(1L).get();
        assertThat(postEntity.getShorts().getShortsStateEnum())
                .isEqualTo(ShortsStateEnum.IN_PROGRESS);
    }

}