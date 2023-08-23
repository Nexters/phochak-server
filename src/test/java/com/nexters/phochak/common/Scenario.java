package com.nexters.phochak.common;

import com.nexters.phochak.auth.presentation.api.LoginApi;
import org.springframework.test.web.servlet.ResultActions;

public class Scenario {

    public static class ScenarioStep {
        public ResultActions response;

        public ScenarioStep(final ResultActions response) {
            this.response = response;
        }

        public Scenario advance() {
            return new Scenario();
        }
        public ResultActions getResponse() {
            return response;
        }
    }
    public static LoginApi login() {
        return new LoginApi();
    }
}
