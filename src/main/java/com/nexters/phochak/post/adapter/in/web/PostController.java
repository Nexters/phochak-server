package com.nexters.phochak.post.adapter.in.web;

import com.nexters.phochak.auth.Auth;
import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.post.application.port.ReportPostUseCase;
import com.nexters.phochak.post.application.port.in.CommonPageResponseDto;
import com.nexters.phochak.post.application.port.in.CommonResponseDto;
import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import com.nexters.phochak.post.application.port.in.LikesUseCase;
import com.nexters.phochak.post.application.port.in.PostCreateRequestDto;
import com.nexters.phochak.post.application.port.in.PostPageResponseDto;
import com.nexters.phochak.post.application.port.in.PostUpdateRequestDto;
import com.nexters.phochak.post.application.port.in.PostUseCase;
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

    private final PostUseCase postUseCase;
    private final LikesUseCase likesUseCase;
    private final ReportPostUseCase reportPostUseCase;

    @Auth
    @GetMapping("/upload-key")
    public CommonResponseDto<PostUploadKeyResponseDto> generateUploadKey(
            @RequestParam(name = "file-extension") String fileExtension) {
        return new CommonResponseDto<>(postUseCase.generateUploadKey(fileExtension));
    }

    @Auth
    @PostMapping
    public CommonResponseDto<Void> createPost(@RequestBody @Valid PostCreateRequestDto postCreateRequestDto) {
        final Long userId = UserContext.getContext();
        postUseCase.create(userId, postCreateRequestDto);
        return new CommonResponseDto<>();
    }

    @Auth
    @GetMapping("/list")
    public CommonPageResponseDto<PostPageResponseDto> getPostList(@Valid CustomCursorDto customCursorDto) {
        final Long userId = UserContext.getContext();
        final List<PostPageResponseDto> nextCursorPage = postUseCase.getPostPage(userId, customCursorDto);
        if (nextCursorPage.size() < customCursorDto.getPageSize()) {
            return new CommonPageResponseDto<>(nextCursorPage, true);
        }
        return new CommonPageResponseDto<>(nextCursorPage);
    }

    @Auth
    @GetMapping("/list/search")
    public CommonPageResponseDto<PostPageResponseDto> getPostListBySearchHashtag(
            @Valid CustomCursorDto customCursorDto,
            @RequestParam(required = false) String hashtag) {
        final Long userId = UserContext.getContext();
        customCursorDto.setHashtag(hashtag);
        final List<PostPageResponseDto> nextCursorPage = postUseCase.getPostPage(userId, customCursorDto);
        if (nextCursorPage.size() < customCursorDto.getPageSize()) {
            return new CommonPageResponseDto<>(nextCursorPage, true);
        }
        return new CommonPageResponseDto<>(nextCursorPage);
    }

    @Auth
    @GetMapping("/hashtag/autocomplete")
    public CommonResponseDto<List<String>> hashtagAutocomplete(@RequestParam String hashtag, @RequestParam int resultSize) {
        return new CommonResponseDto<>(postUseCase.getHashtagAutocomplete(hashtag, resultSize));
    }

    @Auth
    @PutMapping("/{postId}")
    public CommonResponseDto<Void> updatePost(@RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto, @PathVariable Long postId) {
        final Long userId = UserContext.getContext();
        postUseCase.update(userId, postId, postUpdateRequestDto);
        return new CommonResponseDto<>();
    }

    @Auth
    @DeleteMapping("/{postId}")
    public CommonResponseDto<Void> deletePost(@PathVariable Long postId) {
        final Long userId = UserContext.getContext();
        postUseCase.delete(userId, postId);
        return new CommonResponseDto<>();
    }

    @Auth
    @PostMapping("/{postId}/report")
    public CommonResponseDto<Void> reportPost(@PathVariable Long postId) {
        final Long userId = UserContext.getContext();
        reportPostUseCase.processReport(userId, postId);
        return new CommonResponseDto<>();
    }

    @Auth
    @PostMapping("/{postId}/view")
    public CommonResponseDto<Void> updateView(@PathVariable Long postId) {
        postUseCase.updateView(postId);
        return new CommonResponseDto<>();
    }

    @Auth
    @PostMapping("/{postId}/likes")
    public CommonResponseDto<Void> addPhochak(@PathVariable Long postId) {
        final Long userId = UserContext.getContext();
        likesUseCase.addPhochak(userId, postId);
        return new CommonResponseDto<>();
    }

    @Auth
    @DeleteMapping("/{postId}/likes")
    public CommonResponseDto<Void> cancelPhochak(@PathVariable Long postId) {
        final Long userId = UserContext.getContext();
        likesUseCase.cancelPhochak(userId, postId);
        return new CommonResponseDto<>();
    }

}
