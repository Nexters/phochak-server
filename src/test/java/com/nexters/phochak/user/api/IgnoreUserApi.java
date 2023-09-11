package com.nexters.phochak.user.api;

import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IgnoreUserApi {
    private long ignoredUserId = 2L;

    public IgnoreUserApi ignoredUserId(final Long ignoredUserId) {
        this.ignoredUserId = ignoredUserId;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        final ResultActions response = TestUtil.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .post("/v1/user/ignore/{ignoredUserId}", ignoredUserId)
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        return new Scenario.NextScenarioStep(response);
    }
}
