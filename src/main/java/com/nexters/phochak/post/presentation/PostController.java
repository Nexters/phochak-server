package com.nexters.phochak.post.presentation;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.post.CommonPageResponse;
import com.nexters.phochak.post.CommonResponse;
import com.nexters.phochak.post.CustomCursor;
import com.nexters.phochak.post.PostCreateRequestDto;
import com.nexters.phochak.post.PostPageResponseDto;
import com.nexters.phochak.post.PostUpdateRequestDto;
import com.nexters.phochak.post.application.PostService;
import com.nexters.phochak.report.application.ReportPostService;
import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import jakarta.validation.Valid;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;
    private final ReportPostService reportPostService;

    @Auth
    @GetMapping("/upload-key")
    public CommonResponse<PostUploadKeyResponseDto> generateUploadKey(
            @RequestParam(name = "file-extension") String fileExtension) {
        return new CommonResponse<>(postService.generateUploadKey(fileExtension));
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
    public CommonPageResponse<PostPageResponseDto> getPostListBySearchHashtag(@Valid CustomCursor customCursor, @RequestParam(required = false) String hashtag) {
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
