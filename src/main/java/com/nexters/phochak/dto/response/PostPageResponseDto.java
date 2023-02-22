package com.nexters.phochak.dto.response;

import com.nexters.phochak.dto.HashtagFetchDto;
import com.nexters.phochak.dto.LikesFetchDto;
import com.nexters.phochak.dto.PostFetchDto;
import com.nexters.phochak.specification.PostCategoryEnum;
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

    public static PostPageResponseDto of(
            PostFetchDto postFetchDto, HashtagFetchDto hashtagFetchDto, LikesFetchDto likesFetchDto) {
        return PostPageResponseDto.builder()
                .id(postFetchDto.getId())
                .user(postFetchDto.getUser())
                .shorts(postFetchDto.getShorts())
                .hashtags(Objects.isNull(hashtagFetchDto) ? Collections.emptyList() : hashtagFetchDto.getHashtags())
                .view(postFetchDto.getView())
                .category(postFetchDto.getCategory())
                .like(postFetchDto.getLike())
                .isLiked(Objects.isNull(likesFetchDto) ? false : likesFetchDto.isLiked())
                .build();
    }
}
