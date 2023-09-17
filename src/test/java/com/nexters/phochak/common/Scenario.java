package com.nexters.phochak.common;

import com.nexters.phochak.post.api.AddPhochakApi;
import com.nexters.phochak.post.api.CancelPhochakApi;
import com.nexters.phochak.post.api.CreatePostApi;
import com.nexters.phochak.post.api.GetPostApi;
import com.nexters.phochak.shorts.api.EncodingCallbackApi;
import com.nexters.phochak.user.api.IgnoreUserApi;
import com.nexters.phochak.user.api.LoginApi;
import org.springframework.test.web.servlet.ResultActions;

public class Scenario {

    public static IgnoreUserApi ignoreUser() {
        return new IgnoreUserApi();
    }

    public static CreatePostApi createPost() {
        return new CreatePostApi();
    }

    public EncodingCallbackApi encodingCallback() {
        return new EncodingCallbackApi();
    }

    public GetPostApi getPostList() {
        return new GetPostApi();
    }

    public AddPhochakApi addPhochak() {
        return new AddPhochakApi();
    }

    public CancelPhochakApi cancelPhochak() {
        return new CancelPhochakApi();
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
