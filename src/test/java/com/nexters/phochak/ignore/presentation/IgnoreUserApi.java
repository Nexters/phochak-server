package com.nexters.phochak.ignore.presentation;

import com.nexters.phochak.common.TestUtil;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IgnoreUserApi {
    private long ignoredUserId = 2L;

    public IgnoreUserApi ignoredUserId(final Long ignoredUserId) {
        this.ignoredUserId = ignoredUserId;
        return this;
    }

    public void request() throws Exception {
        TestUtil.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/v1/user/ignore/{ignoredUserId}", ignoredUserId)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }
}
