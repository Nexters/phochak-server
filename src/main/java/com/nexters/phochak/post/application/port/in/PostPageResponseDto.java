package com.nexters.phochak.post.application.port.in;

import com.nexters.phochak.likes.LikesFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostPageResponseDto {
    private long id;
    private PostFetchDto.PostUserInformation user;
    private PostFetchDto.PostShortsInformation shorts;
    private List<String> hashtags;
    private long view;
    private PostCategoryEnum category;
    private int like;
    private Boolean isLiked;
    private Boolean isBlind;

    public static PostPageResponseDto of(
            PostFetchDto postFetchDto, HashtagFetchDto hashtagFetchDto, LikesFetchDto likesFetchDto) {
        return PostPageResponseDto.builder()
                .id(postFetchDto.getId())
                .user(postFetchDto.getUser())
                .shorts(postFetchDto.getShorts())
                .hashtags(Objects.isNull(hashtagFetchDto) ? Collections.emptyList() : hashtagFetchDto.getHashtags())
                .view(postFetchDto.getView())
                .category(postFetchDto.getCategory())
                .like(Objects.isNull(likesFetchDto) ? 0 : likesFetchDto.getLike())
                .isLiked(Objects.isNull(likesFetchDto) ? false : likesFetchDto.isLiked())
                .isBlind(postFetchDto.isBlind())
                .build();
    }
}
