package com.nexters.phochak.shorts.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexters.phochak.common.Scenario;
import com.nexters.phochak.common.TestUtil;
import com.nexters.phochak.shorts.application.port.in.EncodingCallbackRequestDto;
import com.nexters.phochak.shorts.domain.EncodingStatusEnum;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.nexters.phochak.auth.AuthAspect.AUTHORIZATION_HEADER;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

public class EncodingCallbackApi {

    private int categoryId = 0;
    private String categoryName = "categoryName";
    private int encodingOptionId = 0;
    private int fileId = 0;
    private String filePath = "/folder/uploadKey_encoded.mp4";
    private String outputType = "outputType";
    private EncodingStatusEnum status = EncodingStatusEnum.WAITING;

    public EncodingCallbackApi categoryId(final int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public EncodingCallbackApi categoryName(final String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public EncodingCallbackApi encodingOptionId(final int encodingOptionId) {
        this.encodingOptionId = encodingOptionId;
        return this;
    }

    public EncodingCallbackApi fileId(final int fileId) {
        this.fileId = fileId;
        return this;
    }

    public EncodingCallbackApi filePath(final String filePath) {
        this.filePath = filePath;
        return this;
    }

    public EncodingCallbackApi filePathByUploadKey(final String key) {
        this.filePath = "/folder/" + key + "_encoded.mp4";
        return this;
    }

    public EncodingCallbackApi outputType(final String outputType) {
        this.outputType = outputType;
        return this;
    }

    public EncodingCallbackApi status(final EncodingStatusEnum status) {
        this.status = status;
        return this;
    }

    public Scenario.NextScenarioStep request() throws Exception {
        final EncodingCallbackRequestDto request = new EncodingCallbackRequestDto(
                categoryId,
                categoryName,
                encodingOptionId,
                fileId,
                filePath,
                outputType,
                status
        );

        final ObjectMapper objectMapper = new ObjectMapper();
        final ResultActions resultActions = TestUtil.mockMvc.perform(post("/v1/post/encoding-callback")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, TestUtil.TestUser.accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        return new Scenario.NextScenarioStep(resultActions);
    }
}
