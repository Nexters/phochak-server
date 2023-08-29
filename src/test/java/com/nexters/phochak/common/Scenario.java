package com.nexters.phochak.common;

import com.nexters.phochak.user.api.LoginApi;
import org.springframework.test.web.servlet.ResultActions;

public class Scenario {

    @lombok.Getter
    public static class ScenarioStep {
        public ResultActions response;

        public ScenarioStep(final ResultActions response) {
            this.response = response;
        }

        public Scenario advance() {
            return new Scenario();
        }

    }
    public static LoginApi login() {
        return new LoginApi();
    }
}
