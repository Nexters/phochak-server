package com.nexters.phochak.common;

import com.nexters.phochak.ignore.presentation.IgnoreUserApi;
import com.nexters.phochak.user.api.LoginApi;
import org.springframework.test.web.servlet.ResultActions;

public class Scenario {

    public static IgnoreUserApi ignoreUser() {
        return new IgnoreUserApi();
    }

    public static class NextScenarioStep {
        public ResultActions response;

        public NextScenarioStep(final ResultActions response) {
            this.response = response;
        }

        public Scenario advance() {
            return new Scenario();
        }

        public ResultActions getResponse() {
            return response;
        }

    }
    public static CreateUserQuery createUser() {
        return new CreateUserQuery();
    }

    public static CreateAccessTokenQuery createAccessToken() {
        return new CreateAccessTokenQuery();
    }

    public static LoginApi login() {
        return new LoginApi();
    }
}
