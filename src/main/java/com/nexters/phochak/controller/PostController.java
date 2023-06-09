package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostFilter;
import com.nexters.phochak.dto.request.PostUpdateRequestDto;
import com.nexters.phochak.dto.response.CommonPageResponse;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.ReportPostService;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.service.ShortsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final ShortsService shortsService;
    private final PostService postService;
    private final ReportPostService reportPostService;

    @Auth
    @GetMapping("/upload-key")
    public CommonResponse<PostUploadKeyResponseDto> generateUploadKey(
            @RequestParam(name = "file-extension") String fileExtension) {
        return new CommonResponse<>(postService.generateUploadKey(fileExtension));
    }

    @PostMapping("/encoding-callback")
    public void encodingCallback(@RequestBody EncodingCallbackRequestDto encodingCallbackRequestDto) {
        if (encodingCallbackRequestDto.getStatus().equals("COMPLETE")) {
            shortsService.connectPost(encodingCallbackRequestDto);
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

    @Auth
    @GetMapping("/list/search")
    public CommonPageResponse<PostPageResponseDto> getPostListBySearchHashtag(@Valid CustomCursor customCursor, @RequestParam String hashtag) {
        List<PostPageResponseDto> nextCursorPage = postService.getNextCursorPage(customCursor, hashtag);
        if (nextCursorPage.size() < customCursor.getPageSize()) {
            return new CommonPageResponse<>(nextCursorPage, true);
        }
        return new CommonPageResponse<>(nextCursorPage);
    }

    @Auth
    @GetMapping("/hashtag/autocomplete")
    public CommonResponse<List<String>> hashtagAutocomplete(@RequestParam String hashtag, @RequestParam int resultSize) {
        return new CommonResponse<>(postService.getHashtagAutocomplete(hashtag, resultSize));
    }

    @Auth
    @PutMapping("/{postId}")
    public CommonResponse<Void> updatePost(@RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto, @PathVariable Long postId) {
        Long userId = UserContext.getContext();
        postService.update(userId, postId, postUpdateRequestDto);
        return new CommonResponse<>();
    }

    @Auth
    @DeleteMapping("/{postId}")
    public CommonResponse<Void> deletePost(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        postService.delete(userId, postId);
        return new CommonResponse<>();
    }

    @Auth
    @PostMapping("/{postId}/report")
    public CommonResponse<Void> reportPost(@PathVariable Long postId) {
        Long userId = UserContext.getContext();
        reportPostService.processReport(userId, postId);
        return new CommonResponse<>();
    }

    @Auth
    @PostMapping("/{postId}/view")
    public CommonResponse<Void> updateView(@PathVariable Long postId) {
        postService.updateView(postId);
        return new CommonResponse<>();
    }

}
