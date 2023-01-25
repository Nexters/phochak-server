package com.nexters.phochak.dto;

import com.nexters.phochak.specification.PostCategoryEnum;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class PostCreateRequestDto {

    private List<String> hashtags;

    @NotBlank
    private MultipartFile shorts;

    @NotBlank
    private PostCategoryEnum postCategory;

}
