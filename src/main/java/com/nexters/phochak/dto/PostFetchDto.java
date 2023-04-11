package com.nexters.phochak.dto;

import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.ShortsStateEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
public class PostFetchDto {
    private long id;
    private PostUserInformation user;
    private PostShortsInformation shorts;
    private long view;
    private PostCategoryEnum category;
    private boolean isBlind;

    @QueryProjection
    public PostFetchDto(long id, PostUserInformation user, PostShortsInformation shorts, long view,
                        PostCategoryEnum category, boolean isBlind) {
        this.id = id;
        this.user = user;
        this.shorts = shorts;
        this.view = view;
        this.category = category;
        this.isBlind = isBlind;
    }

    @Builder
    @NoArgsConstructor
    @Getter
    public static class PostUserInformation {
        private long id;
        private String nickname;
        private String profileImgUrl;

        @QueryProjection
        public PostUserInformation(long id, String nickname, String profileImgUrl) {
            this.id = id;
            this.nickname = nickname;
            this.profileImgUrl = profileImgUrl;
        }
    }

    @Builder
    @NoArgsConstructor
    @Getter
    public static class PostShortsInformation {
        private Long id;
        private ShortsStateEnum state;
        private String shortsUrl;
        private String thumbnailUrl;

        @QueryProjection
        public PostShortsInformation(Long id, ShortsStateEnum state, String shortsUrl, String thumbnailUrl) {
            this.id = id;
            this.state = state;
            this.shortsUrl = shortsUrl;
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
