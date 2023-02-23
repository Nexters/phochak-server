package com.nexters.phochak.service.impl;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.request.PostCreateRequestDto;
import com.nexters.phochak.dto.request.PostFilter;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.HashtagRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.client.StorageBucketClient;
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

@Transactional
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagService hashtagService;
    private final StorageBucketClient storageBucketClient;
    private final ShortsService shortsService;
    private final HashtagRepository hashtagRepository;

    @Override
    public PostUploadKeyResponseDto generateUploadKey(String fileExtension) {
        String uploadKey = generateObjectUploadKey();
        String objectName = uploadKey + "." + fileExtension.toLowerCase();
        return PostUploadKeyResponseDto.builder()
                .uploadKey(uploadKey)
                .uploadUrl(storageBucketClient.generatePresignedUrl(objectName).toString())
                .build();
    }

    @Override
    public void create(Long userId, PostCreateRequestDto postCreateRequestDto) {
        User user = userRepository.getReferenceById(userId);
        Post post = Post.builder()
                .user(user)
                .postCategory(PostCategoryEnum.nameOf(postCreateRequestDto.getCategory()))
                .build();
        postRepository.save(post);
        hashtagService.saveHashtagsByString(postCreateRequestDto.getHashtags(), post);
        shortsService.connectShorts(postCreateRequestDto.getUploadKey(), post);
    }

    @Override
    public void delete(Long userId, Long postId) {
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        if (!post.getUser().equals(user)) {
            throw new PhochakException(ResCode.NOT_POST_OWNER);
        }
        String objectKey = post.getShorts().getUploadKey();
        hashtagRepository.deleteAllByPostId(post.getId());
        postRepository.delete(post);
        storageBucketClient.removeShortsObject(objectKey);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPageResponseDto> getNextCursorPage(CustomCursor customCursor, PostFilter filter) {
        final Long userId = UserContext.CONTEXT.get();

        PostFetchCommand command = PostFetchCommand.of(customCursor, filter, userId);

        return postRepository.findNextPageByCursor(command);
    }

    @Override
    public void viewPost(Long postId) {
        int countOfUpdatedRow = postRepository.updateView(postId);

        if (countOfUpdatedRow < 1) {
            throw new PhochakException(ResCode.NOT_FOUND_POST);
        }
    }

    private String generateObjectUploadKey() {
        return UUID.randomUUID().toString();
    }
}
