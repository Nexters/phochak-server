package com.nexters.phochak.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class EncodingCallbackRequestDto {

    private Integer categoryId;
    private String categoryName;
    private Integer encodingOptionId;
    private Integer fileId;
    private String filePath;
    private String outputType;
    private String status;
}
