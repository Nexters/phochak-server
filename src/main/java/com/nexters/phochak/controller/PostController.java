package com.nexters.phochak.controller;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.auth.annotation.Auth;
import com.nexters.phochak.dto.CommonResponseDto;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postServiceImpl;

    @Auth
    @PostMapping
    public CommonResponseDto createPost(@ModelAttribute @Valid PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContext.getContext();
        postServiceImpl.create(userId, postCreateRequestDto);
        return new CommonResponseDto(ResCode.OK);
    }
}
