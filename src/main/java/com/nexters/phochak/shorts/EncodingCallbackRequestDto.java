package com.nexters.phochak.shorts;

public record EncodingCallbackRequestDto(
        int categoryId,
        String categoryName,
        int encodingOptionId,
        int fileId,
        String filePath,
        String outputType,
        EncodingStatusEnum status
) {
}