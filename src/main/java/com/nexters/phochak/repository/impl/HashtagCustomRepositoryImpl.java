package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.QHashtag;
import com.nexters.phochak.dto.HashtagFetchDto;
import com.nexters.phochak.dto.QHashtagFetchDto;
import com.nexters.phochak.repository.HashtagCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {
    private static final QHashtag hashtag = QHashtag.hashtag;

    private final JPAQueryFactory queryFactory;
    @Override
    public Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds) {
        return queryFactory.from(hashtag)
                .where(hashtag.post.id.in(postIds))
                .transform(groupBy(hashtag.post.id)
                        .as(new QHashtagFetchDto(list(hashtag.tag))));
    }
}
