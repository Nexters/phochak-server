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
import com.nexters.phochak.post.application.port.out.DeleteHashtagPort;
import com.nexters.phochak.post.application.port.out.DeleteMediaPort;
import com.nexters.phochak.post.application.port.out.DeletePostPort;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.application.port.out.LoadUserPort;
import com.nexters.phochak.post.application.port.out.SavePostPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import com.nexters.phochak.shorts.application.ShortsUseCase;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.shorts.presentation.StorageBucketClient;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {
    private final HashtagUseCase hashtagUseCase;
    private final LikesUseCase likesUseCase;
    private final ShortsUseCase shortsUseCase;
    private final StorageBucketClient storageBucketClient;
    private final LoadPostPort loadPostPort;
    private final LoadUserPort loadUserPort;
    private final SavePostPort savePostPort;
    private final DeletePostPort deletePostPort;
    private final DeleteMediaPort deleteMediaPort;

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final ShortsRepository shortsRepository;
    private final DeleteHashtagPort deleteHashtagsPort;

    @Override
    public PostUploadKeyResponseDto generateUploadKey(final String fileExtension) {
        final String uploadKey = generateObjectUploadKey();
        final String objectName = uploadKey + "." + fileExtension.toLowerCase();
        return PostUploadKeyResponseDto.builder()
                .uploadKey(uploadKey)
                .uploadUrl(storageBucketClient.generatePresignedUrl(objectName).toString())
                .build();
    }

    @Override
    @Transactional
    public void create(final Long userId, final PostCreateRequestDto postCreateRequestDto) {
        final User user = loadUserPort.load(userId);
        final Post post = new Post(user, PostCategoryEnum.nameOf(postCreateRequestDto.category()));
        shortsUseCase.connectShorts(post, postCreateRequestDto.uploadKey());
        savePostPort.save(post);
        hashtagUseCase.saveHashtags(post, postCreateRequestDto.hashtags());
    }

    @Override
    @Transactional
    public void update(final Long userId, final Long postId, final PostUpdateRequestDto postUpdateRequestDto) {
        final User user = loadUserPort.load(userId);
        final Post post = loadPostPort.load(postId);
        if (!post.getUser().equals(user)) {
            throw new PhochakException(ResCode.NOT_POST_OWNER);
        }
        post.updateContent(PostCategoryEnum.nameOf(postUpdateRequestDto.category()));
        hashtagUseCase.updateAll(post, postUpdateRequestDto.hashtags());
        savePostPort.save(post);
    }

    @Override
    @Transactional
    public void delete(final Long userId, final Long postId) {
        final User user = loadUserPort.load(userId);
        final Post post = loadPostPort.load(postId);
        if (!post.getUser().equals(user)) {
            throw new PhochakException(ResCode.NOT_POST_OWNER);
        }
        deleteHashtagsPort.deleteAllByPost(post);
        deletePostPort.delete(post);
        deleteMediaPort.deleteShortsMedia(post);
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

    private List<PostPageResponseDto> createPostPageResponseDto(PostFetchCommand command) {
        return switch (command.getFilter()) {
            case SEARCH ->
                    getNextCursorPage(command.getUserId(), hashtagRepository.findSearchedPageByCommmand(command));
            case LIKED -> getNextCursorPage(command.getUserId(), likesUseCase.findLikedPostsByCommand(command));
            default -> getNextCursorPage(command.getUserId(), postRepository.findNextPageByCommmand(command));
        };
    }

    private List<PostPageResponseDto> createPostPageResponseDto(Long userId, List<PostFetchDto> postFetchDtos) {
        List<Long> postIds = postFetchDtos.stream().map(PostFetchDto::getId).collect(Collectors.toList());
        Map<Long, HashtagFetchDto> hashtagFetchDtos = hashtagUseCase.findHashtagsOfPosts(postIds);
        Map<Long, LikesFetchDto> likesFetchDtos = likesUseCase.checkIsLikedPost(postIds, userId);

        return postFetchDtos.stream()
                .map(p -> PostPageResponseDto.of(p, hashtagFetchDtos.get(p.getId()), likesFetchDtos.get(p.getId())))
                .collect(Collectors.toList());
    }

    private List<PostPageResponseDto> getNextCursorPage(Long userId, List<PostFetchDto> postFetchDtos) {
        return createPostPageResponseDto(userId, postFetchDtos);
    }

    private String generateObjectUploadKey() {
        return UUID.randomUUID().toString();
    }
}
