package com.nexters.phochak.auth.presentation.api;

import com.nexters.phochak.common.Scenario;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginApi {
    private String provider = "kakao";
    private String token = "testCode";

    public LoginApi withProvider(final String provider) {
        this.provider = provider;
        return this;
    }

    public LoginApi withToken(final String token) {
        this.token = token;
        return this;
    }

    public Scenario.ScenarioStep request(final MockMvc mockMvc) throws Exception {
        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v2/auth/login/{provider}", provider)
                                .param("token", token)
                                .param("fcmDeviceToken", "TestFcmDeviceToken"))
                .andExpect(status().isOk());
        return new Scenario.ScenarioStep(response);
    }

}
