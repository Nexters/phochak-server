package com.nexters.phochak.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class EncodingCallbackRequestDto {

    private Integer categoryId;
    private String categoryName;
    private Integer encodingOptionId;
    private Integer fileId;
    private String filePath;
    private String outputType;
    private String status;

    @Builder
    public EncodingCallbackRequestDto(Integer categoryId, String categoryName, Integer encodingOptionId, Integer fileId, String filePath, String outputType, String status) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.encodingOptionId = encodingOptionId;
        this.fileId = fileId;
        this.filePath = filePath;
        this.outputType = outputType;
        this.status = status;
    }
}
