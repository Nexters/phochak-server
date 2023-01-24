package com.nexters.phochak.controller;

import com.nexters.phochak.dto.CommonResponseDto;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postServiceImpl;

    @PostMapping
    public ResponseEntity<CommonResponseDto> createPost(@AuthenticatedUser LoginUser loginUser, @Valid PostCreateRequestDto postCreateRequestDto) {
        postServiceImpl.create(loginUser, postCreateRequestDto);
        return ResponseEntity.ok(new CommonResponseDto(ResCode.OK));
    }
}
