package com.nexters.phochak.post.api;

import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static com.nexters.phochak.common.exception.ResCode.OK;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AddPhochakApi {
    private Long postId = 1L;

    public AddPhochakApi postId(final Long postId) {
        this.postId = postId;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        final ResultActions response = TestUtil.mockMvc.perform(post("/v1/post/{postId}/likes/", postId)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.resCode").value(OK.getCode()));

        return new Scenario.NextScenarioStep(response);
    }
}
