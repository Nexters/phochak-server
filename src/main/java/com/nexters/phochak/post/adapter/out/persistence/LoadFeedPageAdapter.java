package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import com.nexters.phochak.post.application.port.in.PostFetchDto;
import com.nexters.phochak.post.application.port.out.LoadFeedPagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadFeedPageAdapter implements LoadFeedPagePort {
    private final HashtagRepository hashtagRepository;
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    @Override
    public List<PostFetchDto> searchPagingByHashtag(final Long userId, final CustomCursorDto customCursorDto) {
        return hashtagRepository.searchPagingByHashtag(userId, customCursorDto);
    }

    @Override
    public List<PostFetchDto> pagingPostsByLikes(final Long userId, final CustomCursorDto customCursorDto) {
        return likesRepository.pagingPostsByLikes(userId, customCursorDto);
    }

    @Override
    public List<PostFetchDto> pagingPost(final Long userId, final CustomCursorDto customCursorDto) {
        return postRepository.pagingPost(userId, customCursorDto);
    }
}
