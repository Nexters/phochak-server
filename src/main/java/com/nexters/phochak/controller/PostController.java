package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.response.CommonPageResponse;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.service.impl.NCPShortsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final NCPShortsService ncpShortsService;
    private final PostService postService;

    @Auth
    @GetMapping("/upload-key")
    public CommonResponse<PostUploadKeyResponseDto> generateUploadKey(
            @RequestParam(name="file-extension") String fileExtension) {
        return new CommonResponse<>(postServiceImpl.generateUploadKey(fileExtension));
    }

    @PostMapping("/encoding-callback")
    public void encodingCallback(@RequestBody EncodingCallbackRequestDto encodingCallbackRequestDto) {
        if(encodingCallbackRequestDto.getStatus().equals("COMPLETE")) {
            ncpShortsService.connectPost(encodingCallbackRequestDto);
        }
    }

    @Auth
    @PostMapping
    public CommonResponse<Void> createPost(@RequestBody @Valid PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContext.getContext();
        postService.create(userId, postCreateRequestDto);
        return new CommonResponse<>();
    }

    @Auth
    @GetMapping("/list")
    public CommonPageResponse<PostPageResponseDto> getPostList(@Valid CustomCursor customCursor) {
        List<PostPageResponseDto> nextCursorPage = postService.getNextCursorPage(customCursor);
        if (nextCursorPage.size() < customCursor.getPageSize()) {
            return new CommonPageResponse<>(nextCursorPage, true);
        }
        return new CommonPageResponse<>(nextCursorPage);
    }
}
