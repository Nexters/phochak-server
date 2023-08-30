package com.nexters.phochak.common;

import com.nexters.phochak.user.api.LoginApi;
import org.springframework.test.web.servlet.ResultActions;

public class Scenario {

    @lombok.Getter
    public static class NextScenarioStep {
        public ResultActions response;

        public NextScenarioStep(final ResultActions response) {
            this.response = response;
        }

        public Scenario advance() {
            return new Scenario();
        }

    }
    public static CreateUserQuery createUser() {
        return new CreateUserQuery();
    }
    public static LoginApi login() {
        return new LoginApi();
    }
}
