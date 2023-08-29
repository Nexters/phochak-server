package com.nexters.phochak.user.api;

import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.notification.domain.OperatingSystem;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginApi {
    private String provider = "kakao";
    private String token = "testCode";
    private String fcmDeviceToken = "TestFcmDeviceToken";
    private String operatingSystem = OperatingSystem.IOS.toString();

    public LoginApi provider(final String provider) {
        this.provider = provider;
        return this;
    }

    public LoginApi token(final String token) {
        this.token = token;
        return this;
    }

    public LoginApi fcmDeviceToken(final String fcmDeviceToken) {
        this.fcmDeviceToken = fcmDeviceToken;
        return this;
    }

    public LoginApi operatingSystem(final String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public Scenario.ScenarioStep request(final MockMvc mockMvc) throws Exception {

        final ResultActions response = mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v2/auth/login/{provider}", provider)
                                .param("token", token)
                                .param("fcmDeviceToken", fcmDeviceToken)
                                .param("operatingSystem", operatingSystem))
                .andExpect(status().isOk());
        return new Scenario.ScenarioStep(response);
    }

}
