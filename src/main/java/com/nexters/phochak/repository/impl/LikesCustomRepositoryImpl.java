package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.QLikes;
import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.domain.QShorts;
import com.nexters.phochak.dto.LikesFetchDto;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;
import com.nexters.phochak.dto.QPostFetchDto;
import com.nexters.phochak.dto.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.dto.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.dto.QQuerydslFetchDto;
import com.nexters.phochak.dto.QuerydslFetchDto;
import com.nexters.phochak.repository.LikesCustomRepository;
import com.nexters.phochak.specification.PostSortOption;
import com.nexters.phochak.specification.ShortsStateEnum;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class LikesCustomRepositoryImpl implements LikesCustomRepository {
    private static final QLikes likes = QLikes.likes;
    private static final QPost post = QPost.post;
    private static final QShorts shorts = QShorts.shorts;

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId) {
        Map<Long, LikesFetchDto> result = new HashMap<>();

        Map<Long, List<QuerydslFetchDto>> map = queryFactory.from(likes)
                .join(likes.post)
                .where(likes.post.id.in(postIds))
                .transform(groupBy(likes.post.id)
                        .as(list(new QQuerydslFetchDto(likes.user.id.eq(userId)))));

        map.keySet().forEach(k -> {
                    int size = map.get(k).size();
                    boolean isLiked = false;
                    for (int i = 0; i < size; i++) {
                        if (map.get(k).get(i).isLiked()) {
                            isLiked = true;
                            break;
                        }}
                    result.put(k, new LikesFetchDto(size, isLiked));
                });

        return result;
    }

    @Override
    public List<PostFetchDto> findLikedPosts(PostFetchCommand command) {
        Map<Long, PostFetchDto> result = queryFactory.select(likes, post)
                .from(likes)
                .join(post).on(likes.post.eq(post))
                .join(likes.user)
                .join(likes.post.shorts)
                .join(shorts).on(likes.post.shorts.eq(shorts))
                .where(likes.user.id.eq(command.getUserId()))
                .where(filterByCursor(command))
                .where(post.shorts.shortsStateEnum.eq(ShortsStateEnum.OK)) // shorts의 인코딩이 완료된 게시글
                .limit(command.getPageSize())
                .orderBy(orderByPostSortOption(command.getSortOption())) // 커서 정렬 조건
                .orderBy(post.id.desc())
                .groupBy(post.id)
                .transform(groupBy(post.id).as(
                        new QPostFetchDto(post.id,
                                new QPostFetchDto_PostUserInformation(likes.user.id, likes.user.nickname, likes.user.profileImgUrl),
                                new QPostFetchDto_PostShortsInformation(shorts.id, shorts.shortsStateEnum, shorts.shortsUrl, shorts.thumbnailUrl),
                                likes.post.view, likes.post.postCategory)
                ));

        return result.keySet().stream()
                .map(result::get)
                .collect(Collectors.toList());
    }

    private BooleanExpression filterByCursor(PostFetchCommand command) {
        BooleanExpression defaultFilter = likes.post.id.lt(command.getLastId());
        switch (command.getSortOption()) {
            case VIEW:
                return likes.post.view.loe(command.getSortValue()).and(defaultFilter);
            case LIKE:
                return likes.count().loe(command.getSortValue()).and(defaultFilter);
            default:
                return defaultFilter;
        }
    }

    private static OrderSpecifier orderByPostSortOption(PostSortOption postSortOption) {
        if (postSortOption == PostSortOption.LIKE) {
            return likes.count().desc();
        } else if (postSortOption == PostSortOption.VIEW) {
            return likes.post.view.desc();
        }
        return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }
}
