package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.application.port.in.CustomCursorDto;
import com.nexters.phochak.post.application.port.in.PostFetchDto;

import java.util.List;

public interface GetFeedPagePort {
    List<PostFetchDto> searchPagingByHashtag(Long userId, CustomCursorDto customCursorDto);

    List<PostFetchDto> pagingPostsByLikes(Long userId, CustomCursorDto customCursorDto);

    List<PostFetchDto> pagingPost(Long userId, CustomCursorDto customCursorDto);
}
