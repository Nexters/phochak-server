package com.nexters.phochak.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.RestDocsApiTest;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.adapter.in.web.PostController;
import com.nexters.phochak.post.application.port.in.PostCreateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends RestDocsApiTest {
    @Autowired
    PostController postController;
    @Autowired
    ObjectMapper objectMapper;
    MockMvc mockMvc;

    @BeforeEach
    void setUpMock(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvcBuilder(restDocumentation, postController).build();
        TestUtil.setMockMvc(mockMvc);
    }

    @Test
    @DisplayName("포스트 API - 게시글 생성 성공")
    void createPost_success() throws Exception {
        //given
//        Map<String, Object> body = new HashMap<>();
//        body.put("uploadKey", "uploadKey");
//        body.put("category", "RESTAURANT");
//        body.put("hashtags", List.of("해시태그1", "해시태그2", "해시태그3"));
        final PostCreateRequestDto request = new PostCreateRequestDto(
                "uploadKey",
                List.of("해시태그1", "해시태그2", "해시태그3"),
                "RESTAURANT"
        );

        // when, then
        mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));
//                .andDo(document("post/POST",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("category").description("카테고리 ex) TOUR / RESTAURANT / CAFE"),
//                                fieldWithPath("uploadKey").description("발급 받았던 업로드 키"),
//                                fieldWithPath("hashtags").description("해시태그 배열 ex) [\"해시태그1\", \"해시태그2\", \"해시태그3\"))]")
//                        ),
//                        requestHeaders(
//                                headerWithName(AUTHORIZATION_HEADER)
//                                        .description("JWT Access Token")
//                        ),
//                        responseFields(
//                                fieldWithPath("status.resCode").type(JsonFieldType.STRING).description("응답 코드"),
//                                fieldWithPath("status.resMessage").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.NULL).description("null")
//                        )
//                ));
    }
}
