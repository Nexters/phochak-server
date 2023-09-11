package com.nexters.phochak.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.application.port.in.PostCreateRequestDto;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreatePostApi {
    private String uploadKey = "uploadKey";
    private List<String> hashtagList = List.of("해시태그1", "해시태그2", "해시태그3");
    private PostCategoryEnum postCategoryEnum = PostCategoryEnum.RESTAURANT;

    public CreatePostApi uploadKey(final String uploadKey) {
        this.uploadKey = uploadKey;
        return this;
    }

    public CreatePostApi hashtagList(final List<String> hashtagList) {
        this.hashtagList = hashtagList;
        return this;
    }

    public CreatePostApi postCategoryEnum(final PostCategoryEnum postCategoryEnum) {
        this.postCategoryEnum = postCategoryEnum;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        final PostCreateRequestDto request = new PostCreateRequestDto(
                uploadKey,
                hashtagList,
                postCategoryEnum.name()
        );

        // when, then
        ObjectMapper objectMapper = new ObjectMapper();
        final ResultActions response = TestUtil.mockMvc.perform(post("/v1/post")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));

        return new Scenario.NextScenarioStep(response);
    }
}
