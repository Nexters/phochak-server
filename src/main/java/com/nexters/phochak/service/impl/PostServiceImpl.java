package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.dto.PostCreateRequestDto;
import com.nexters.phochak.dto.PostUploadKeyRequestDto;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.StorageBucketRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.HashtagService;
import com.nexters.phochak.service.PostService;
import com.nexters.phochak.service.ShortsService;
import com.nexters.phochak.specification.PostCategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ShortsService shortsService;
    private final HashtagService hashtagService;
    private final StorageBucketRepository storageBucketRepository;

    public PostUploadKeyResponseDto generateUploadKey(Long userId, PostUploadKeyRequestDto postUploadKeyRequestDto) {
        String objectName = generateObjectName(postUploadKeyRequestDto.getFileExtension());
        return PostUploadKeyResponseDto.builder()
                .objectName(objectName)
                .uploadUrl(storageBucketRepository.generatePresignedUrl(objectName).toString())
                .build();
    }

    @Override
    @Transactional
    public void create(Long userId, PostCreateRequestDto postCreateRequestDto) {
        User user = userRepository.getReferenceById(userId);
        Shorts shorts = shortsService.createShorts(postCreateRequestDto);
        Post post = Post.builder()
                        .user(user)
                        .postCategory(PostCategoryEnum.nameOf(postCreateRequestDto.getPostCategory()))
                        .shorts(shorts)
                        .build();
        hashtagService.createHashtagsByString(postCreateRequestDto.getHashtags(), post);
        postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPageResponseDto> getNextCursorPage(CustomCursor customCursor) {
        return postRepository.findNextPageByCursor(customCursor);
    }
    
    private String generateObjectName(String fileExtension) {
        return UUID.randomUUID() + "." + fileExtension;
    }

}
