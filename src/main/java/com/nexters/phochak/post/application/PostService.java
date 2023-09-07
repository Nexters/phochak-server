package com.nexters.phochak.post.application;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostFetchCommand;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import com.nexters.phochak.post.application.port.in.HashtagUseCase;
import com.nexters.phochak.post.application.port.in.LikesFetchDto;
import com.nexters.phochak.post.application.port.in.LikesUseCase;
import com.nexters.phochak.post.application.port.in.PostCreateRequestDto;
import com.nexters.phochak.post.application.port.in.PostFetchDto;
import com.nexters.phochak.post.application.port.in.PostPageResponseDto;
import com.nexters.phochak.post.application.port.in.PostUpdateRequestDto;
import com.nexters.phochak.post.application.port.in.PostUseCase;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import com.nexters.phochak.shorts.application.ShortsService;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.shorts.presentation.StorageBucketClient;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagUseCase hashtagUseCase;
    private final StorageBucketClient storageBucketClient;
    private final ShortsService shortsService;
    private final LikesUseCase likesUseCase;
    private final HashtagRepository hashtagRepository;
    private final ShortsRepository shortsRepository;

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
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = PostEntity.builder()
                .userEntity(userEntity)
                .postCategory(PostCategoryEnum.nameOf(postCreateRequestDto.getCategory()))
                .build();
        postRepository.save(postEntity);
        hashtagUseCase.saveHashtagsByString(postCreateRequestDto.getHashtags(), postEntity);
        shortsService.connectShorts(postCreateRequestDto.getUploadKey(), postEntity);
    }

    @Override
    public void update(Long userId, Long postId, PostUpdateRequestDto postUpdateRequestDto) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        if (!postEntity.getUser().equals(userEntity)) {
            throw new PhochakException(ResCode.NOT_POST_OWNER);
        }
        postEntity.updateContent(PostCategoryEnum.nameOf(postUpdateRequestDto.getCategory()));
        hashtagUseCase.updateAll(postEntity, postUpdateRequestDto.getHashtags());
    }

    @Override
    public void delete(Long userId, Long postId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        if (!postEntity.getUser().equals(userEntity)) {
            throw new PhochakException(ResCode.NOT_POST_OWNER);
        }
        String objectKey = postEntity.getShorts().getUploadKey();
        hashtagRepository.deleteAllByPostId(postEntity.getId());
        postRepository.delete(postEntity);
        storageBucketClient.removeShortsObject(List.of(objectKey));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPageResponseDto> getNextCursorPage(CustomCursorDto customCursorDto) {
        final Long userId = UserContext.CONTEXT.get();

        PostFetchCommand command = PostFetchCommand.of(customCursorDto, userId);

        return createPostPageResponseDto(command);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPageResponseDto> getNextCursorPage(CustomCursorDto customCursorDto, String hashtag) {
        final Long userId = UserContext.CONTEXT.get();

        PostFetchCommand command = PostFetchCommand.of(customCursorDto, userId, hashtag);

        return createPostPageResponseDto(command);
    }

    private List<PostPageResponseDto> createPostPageResponseDto(PostFetchCommand command) {
        switch(command.getFilter()) {
            case SEARCH:
                return getNextCursorPage(command.getUserId(), hashtagRepository.findSearchedPageByCommmand(command));
            case LIKED:
                return getNextCursorPage(command.getUserId(), likesUseCase.findLikedPostsByCommand(command));
            case UPLOADED:
            case NONE:
            default:
                return getNextCursorPage(command.getUserId(), postRepository.findNextPageByCommmand(command));
        }
    }

    private List<PostPageResponseDto> getNextCursorPage(Long userId, List<PostFetchDto> postFetchDtos) {
        return createPostPageResponseDto(userId, postFetchDtos);
    }

    private List<PostPageResponseDto> createPostPageResponseDto(Long userId, List<PostFetchDto> postFetchDtos) {
        List<Long> postIds = postFetchDtos.stream().map(PostFetchDto::getId).collect(Collectors.toList());
        Map<Long, HashtagFetchDto> hashtagFetchDtos = hashtagUseCase.findHashtagsOfPosts(postIds);
        Map<Long, LikesFetchDto> likesFetchDtos = likesUseCase.checkIsLikedPost(postIds, userId);

        return postFetchDtos.stream()
                .map(p -> PostPageResponseDto.of(p, hashtagFetchDtos.get(p.getId()), likesFetchDtos.get(p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public int updateView(Long postId) {
        int countOfUpdatedRow = postRepository.updateView(postId);

        if (countOfUpdatedRow < 1) {
            throw new PhochakException(ResCode.NOT_FOUND_POST);
        }
        return countOfUpdatedRow;
    }

    @Override
    public void deleteAllPostByUser(UserEntity userEntity) {
        List<PostEntity> postEntityList = postRepository.findAllPostByUserFetchJoin(userEntity);
        List<Long> postIdList = postEntityList.stream().map(PostEntity::getId).collect(Collectors.toList());
        List<String> shortsKeyList = postEntityList.stream().map(post -> post.getShorts().getUploadKey()).collect(Collectors.toList());
        postRepository.deleteAllByUser(userEntity);
        shortsRepository.deleteAllByUploadKeyIn(shortsKeyList);
        hashtagRepository.deleteAllByPostIdIn(postIdList);
        storageBucketClient.removeShortsObject(shortsKeyList);
    }

    @Override
    public List<String> getHashtagAutocomplete(String hashtag, int resultSize) {
        Pageable pageable = PageRequest.of(0, resultSize);
        return hashtagRepository.findByHashtagStartsWith(hashtag, pageable);
    }

    private String generateObjectUploadKey() {
        return UUID.randomUUID().toString();
    }
}
