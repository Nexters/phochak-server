package com.nexters.phochak.post.api;

import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.post.adapter.out.persistence.PostFilter;
import com.nexters.phochak.post.adapter.out.persistence.PostSortOption;
import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetPostApi {

    private PostSortOption postSortOption = PostSortOption.LATEST;
    private PostFilter postFilter = PostFilter.NONE;
    private int pageSize = 2;
    private Long lastId = null;

    public GetPostApi postSortOption(final PostSortOption postSortOption) {
        this.postSortOption = postSortOption;
        return this;
    }

    public GetPostApi postFilter(final PostFilter postFilter) {
        this.postFilter = postFilter;
        return this;
    }

    public GetPostApi pageSize(final int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public GetPostApi lastId(final long lastId) {
        this.lastId = lastId;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        CustomCursorDto customCursorDto = CustomCursorDto.builder()
                .sortOption(postSortOption)
                .filter(postFilter)
                .pageSize(pageSize)
                .lastId(lastId)
                .build();

        final ResultActions response = TestUtil.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get("/v1/post/list")
                                .param("sortOption", customCursorDto.getSortOption().name())
                                .param("pageSize", String.valueOf(customCursorDto.getPageSize()))
                                .param("lastId", String.valueOf(customCursorDto.getLastId()))
                                .param("filter", customCursorDto.getFilter().name())
                                .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(status().isOk());

        return new Scenario.NextScenarioStep(response);
    }
}
