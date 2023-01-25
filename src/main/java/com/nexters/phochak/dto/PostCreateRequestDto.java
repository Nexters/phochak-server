package com.nexters.phochak.dto;

import com.nexters.phochak.specification.PostCategory;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class PostCreateRequestDto {

    private List<String> hashtags;

    @NotBlank
    private MultipartFile multipartFile;

    @NotBlank
    private PostCategory postCategory;

}
