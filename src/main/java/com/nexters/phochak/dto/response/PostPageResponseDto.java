package com.nexters.phochak.dto.response;

import com.nexters.phochak.domain.Hashtag;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.PostCategoryEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPageResponseDto {
    private long id;
    private User user;
    private Shorts shorts;
    private List<String> hashtags;
    private long view;
    private PostCategoryEnum category;
    private long like;
    private Boolean isLiked;

    public static PostPageResponseDto from(Post post, Long userId) {
        return PostPageResponseDto.builder()
                .id(post.getId())
                .user(post.getUser())
                .shorts(post.getShorts())
                .hashtags(post.getHashtags().stream().map(Hashtag::getTag).collect(Collectors.toList()))
                .view(post.getView())
                .category(post.getPostCategory())
                .like(post.getLikes().size())
                .isLiked(post.getLikes().stream().anyMatch(likes -> likes.hasLikesByUser(userId)))
                .build();
    }

    @Getter
    private static class HashTagsDto {
        private final List<String> tags;

        private HashTagsDto(List<String> tags) {
            this.tags = tags;
        }

        public static HashTagsDto from(List<Hashtag> hashtags) {
            List<String> tags = hashtags.stream().map(Hashtag::getTag).collect(Collectors.toList());
            return new HashTagsDto(tags);
        }
    }
}
