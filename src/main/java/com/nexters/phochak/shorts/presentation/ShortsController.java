package com.nexters.phochak.shorts.presentation;

import com.nexters.phochak.shorts.EncodingCallbackRequestDto;
import com.nexters.phochak.shorts.application.ShortsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ShortsController {
    private final ShortsUseCase shortsUseCase;
    @PostMapping("/v1/post/encoding-callback")
    public void encodingCallback(@RequestBody EncodingCallbackRequestDto encodingCallbackRequestDto) {
        //TODO: url 수정 필요 post -> shorts
        shortsUseCase.processPost(encodingCallbackRequestDto);
    }
}
