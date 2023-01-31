package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.repository.PostCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private static final QPost post = QPost.post;

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostPageResponseDto> findNextPageByCursor(Long lastId, Long pageSize, String postSortCriteria, Long lastCriteriaValue) {

        List<Post> result = queryFactory.select(post)
                .from(post)
                .limit(pageSize)
                .fetch();

        log.debug("findBy {}", result);
        return null;
    }
}
