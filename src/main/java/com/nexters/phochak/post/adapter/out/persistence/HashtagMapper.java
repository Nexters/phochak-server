package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.domain.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashtagMapper {

    private final PostMapper postMapper;

    public HashtagEntity toEntity(final Hashtag hashtag) {
        return new HashtagEntity(
                hashtag.getId(),
                postMapper.toEntity(hashtag.getPost()),
                hashtag.getTag());
    }

    public Hashtag toDomain(final HashtagEntity hashtagEntity) {
        return Hashtag.forMapper(
                hashtagEntity.getId(),
                postMapper.toDomain(hashtagEntity.getPost()),
                hashtagEntity.getTag());
    }
}
