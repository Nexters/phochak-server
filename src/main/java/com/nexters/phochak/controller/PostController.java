package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.PostPageRequestDto;
import com.nexters.phochak.dto.response.CommonListResponse;
import com.nexters.phochak.dto.response.CommonResponse;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;

    @Auth
    @PostMapping
    public CommonResponse<Void> createPost(@ModelAttribute @Valid PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContext.getContext();
        postService.create(userId, postCreateRequestDto);
        return new CommonResponse<>();
    }

    @GetMapping
    public CommonListResponse<PostPageResponseDto> getPostList(@Valid PostPageRequestDto postPageRequestDto) {
        List<PostPageResponseDto> nextCursorPage = postService.getNextCursorPage(
                postPageRequestDto.getLastId(), postPageRequestDto.getPageSize(), postPageRequestDto.getPostSortCriteria(), postPageRequestDto.getLastCriteriaValue());
        return new CommonListResponse<>(nextCursorPage);
    }
}
