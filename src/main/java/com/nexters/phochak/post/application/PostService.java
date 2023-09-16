package com.nexters.phochak.post.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.LikesRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
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
import com.nexters.phochak.post.application.port.out.GeneratePresignedUrlPort;
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

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService implements PostUseCase {
    private final HashtagUseCase hashtagUseCase;
    private final LikesUseCase likesUseCase;
    private final ShortsUseCase shortsUseCase;
    private final LoadPostPort loadPostPort;
    private final LoadUserPort loadUserPort;
    private final SavePostPort savePostPort;
    private final DeletePostPort deletePostPort;
    private final DeleteMediaPort deleteMediaPort;
    private final GeneratePresignedUrlPort generatePresignedUrlPort;

    private final StorageBucketClient storageBucketClient;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final ShortsRepository shortsRepository;
    private final LikesRepository likesRepository;
    private final DeleteHashtagPort deleteHashtagsPort;

    @Override
    public PostUploadKeyResponseDto generateUploadKey(final String fileExtension) {
        final String uploadKey = UUID.randomUUID().toString();
        final URL url = generatePresignedUrlPort.generate(uploadKey, fileExtension);
        return new PostUploadKeyResponseDto(url, uploadKey);
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
    public List<PostPageResponseDto> getNextCursorPage(final Long userId, final CustomCursorDto customCursorDto) {
        return switch (customCursorDto.getFilter()) {
            case SEARCH -> getNextCursorPage(userId, hashtagRepository.searchPagingByHashtag(userId, customCursorDto));
            case LIKED -> getNextCursorPage(userId, likesRepository.pagingPostsByLikes(userId, customCursorDto));
            default -> getNextCursorPage(userId, postRepository.pagingPost(userId, customCursorDto));
        };
    }

    @Override
    public int updateView(final Long postId) {
        final int countOfUpdatedRow = postRepository.updateView(postId);
        if (countOfUpdatedRow < 1) {
            throw new PhochakException(ResCode.NOT_FOUND_POST);
        }
        return countOfUpdatedRow;
    }

    @Override
    public void deleteAllPostByUser(final UserEntity userEntity) {
        final List<PostEntity> postEntityList = postRepository.findAllPostByUserFetchJoin(userEntity);
        final List<Long> postIdList = postEntityList.stream().map(PostEntity::getId).toList();
        final List<String> shortsKeyList = postEntityList.stream().map(post -> post.getShorts().getUploadKey()).toList();
        postRepository.deleteAllByUser(userEntity);
        shortsRepository.deleteAllByUploadKeyIn(shortsKeyList);
        hashtagRepository.deleteAllByPostIdIn(postIdList);
        storageBucketClient.removeShortsObject(shortsKeyList);
    }

    @Override
    public List<String> getHashtagAutocomplete(final String hashtag, final int resultSize) {
        final Pageable pageable = PageRequest.of(0, resultSize);
        return hashtagRepository.findByHashtagStartsWith(hashtag, pageable);
    }

    private List<PostPageResponseDto> createPostPageResponseDto(final Long userId, final List<PostFetchDto> postFetchDtos) {
        final List<Long> postIds = postFetchDtos.stream().map(PostFetchDto::getId).toList();
        final Map<Long, HashtagFetchDto> hashtagFetchDtos = hashtagUseCase.findHashtagsOfPosts(postIds);
        final Map<Long, LikesFetchDto> likesFetchDtos = likesUseCase.checkIsLikedPost(postIds, userId);

        return postFetchDtos.stream()
                .map(p -> PostPageResponseDto.of(
                        p,
                        hashtagFetchDtos.get(p.getId()),
                        likesFetchDtos.get(p.getId()))
                ).toList();
    }

    private List<PostPageResponseDto> getNextCursorPage(final Long userId, final List<PostFetchDto> postFetchDtos) {
        return createPostPageResponseDto(userId, postFetchDtos);
    }

}
