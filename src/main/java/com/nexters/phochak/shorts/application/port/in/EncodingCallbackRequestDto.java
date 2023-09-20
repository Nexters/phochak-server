package com.nexters.phochak.shorts.application.port.in;

import com.nexters.phochak.shorts.domain.EncodingStatusEnum;

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