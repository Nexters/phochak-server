package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.response.CommonPageResponse;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.PostService;
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

    private final PostService postService;

//    @Auth
    @GetMapping("/upload-key")
    public CommonResponse<PostUploadKeyResponseDto> generateUploadKey(
            @RequestParam(name="file-extension") String fileExtension) {
        return new CommonResponse<>(postServiceImpl.generateUploadKey(fileExtension));
    }

    @Auth
    @PostMapping
    public CommonResponse<Void> createPost(@ModelAttribute @Valid PostCreateRequestDto postCreateRequestDto) {
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
