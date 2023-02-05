package com.nexters.phochak.dto.response;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.PostCategoryEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostPageResponseDto {
    private long id;
    private long userId;
    private User user;
    private Shorts shorts;
    private long view;
    private PostCategoryEnum postCategoryEnum;
    private long phochakCount;

    public static PostPageResponseDto from(Post post) {
        return PostPageResponseDto.builder()
                .id(post.getId())
                .user(post.getUser())
                .userId(post.getUser().getId())
                .shorts(post.getShorts())
                .view(post.getView())
                .postCategoryEnum(post.getPostCategory())
                .phochakCount(post.getPhochaks().size())
                .build();
    }
}
