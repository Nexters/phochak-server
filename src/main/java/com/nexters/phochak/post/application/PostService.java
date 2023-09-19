package com.nexters.phochak.post.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
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
import com.nexters.phochak.post.application.port.out.DeleteShortsPort;
import com.nexters.phochak.post.application.port.out.GeneratePresignedUrlPort;
import com.nexters.phochak.post.application.port.out.GetHashtagAutocompletePort;
import com.nexters.phochak.post.application.port.out.LoadFeedPagePort;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.application.port.out.LoadUserPort;
import com.nexters.phochak.post.application.port.out.RemoveShortsObjectPort;
import com.nexters.phochak.post.application.port.out.SavePostPort;
import com.nexters.phochak.post.application.port.out.UpdateViewPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import com.nexters.phochak.shorts.application.ShortsUseCase;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
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
    private final LoadFeedPagePort loadFeedPagePort;
    private final LoadUserPort loadUserPort;
    private final SavePostPort savePostPort;
    private final DeletePostPort deletePostPort;
    private final DeleteMediaPort deleteMediaPort;
    private final GetHashtagAutocompletePort getHashtagAutocompletePort;
    private final GeneratePresignedUrlPort generatePresignedUrlPort;
    private final DeleteHashtagPort deleteHashtagsPort;
    private final UpdateViewPort updateViewPort;
    private final DeleteShortsPort deleteShortsPort;
    private final RemoveShortsObjectPort removeShortsObjectPort;

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
    public List<PostPageResponseDto> getPostPage(final Long userId, final CustomCursorDto customCursorDto) {
        final List<PostFetchDto> result = switch (customCursorDto.getFilter()) {
            case SEARCH -> loadFeedPagePort.searchPagingByHashtag(userId, customCursorDto);
            case LIKED -> loadFeedPagePort.pagingPostsByLikes(userId, customCursorDto);
            default -> loadFeedPagePort.pagingPost(userId, customCursorDto);
        };
        return getNextCursorPage(userId, result);
    }

    @Override
    public void updateView(final Long postId) {
        updateViewPort.increaseView(postId);
    }

    @Override
    public void deleteAllPost(final Long userId) {
        final User user = loadUserPort.load(userId);
        List<Post> postList = loadPostPort.loadAllPostByUser(user);
        deletePostPort.deleteAllByUser(user);
        deleteShortsPort.deleteAllIn(postList);
        deleteHashtagsPort.deleteAllByPostIdIn(postList);
        removeShortsObjectPort.remove(postList);
    }

    @Override
    public List<String> getHashtagAutocomplete(final String hashtag, final int resultSize) {
        return getHashtagAutocompletePort.search(hashtag, resultSize);
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
