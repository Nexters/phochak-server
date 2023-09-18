package com.nexters.phochak.post.adapter.out.persistence;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class HashtagFetchDto {
    List<String> hashtags;

    public static HashtagFetchDto from(List<HashtagEntity> hashtagEntity) {
        List<String> hashtags = hashtagEntity.stream()
                .map(HashtagEntity::getTag)
                .collect(Collectors.toList());

        return HashtagFetchDto.builder()
                .hashtags(hashtags)
                .build();
    }

    @QueryProjection
    public HashtagFetchDto(List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
