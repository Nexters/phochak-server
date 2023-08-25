package com.nexters.phochak.common.docs;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
public class RestDocs {
    private MockMvcConfigurer apply(RestDocumentationContextProvider restDocumentation) {
        return documentationConfiguration(restDocumentation);
    }

    protected DefaultMockMvcBuilder getMockMvcBuilder(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context).apply(apply(restDocumentation));
    }

    protected StandaloneMockMvcBuilder getMockMvcBuilder(RestDocumentationContextProvider restDocumentation, Object... controllers) {
        return MockMvcBuilders.standaloneSetup(controllers).apply(apply(restDocumentation));
    }
}
