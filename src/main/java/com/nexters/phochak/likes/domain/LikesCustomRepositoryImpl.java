package com.nexters.phochak.likes.domain;

import com.nexters.phochak.likes.LikesFetchDto;
import com.nexters.phochak.post.QPostFetchDto;
import com.nexters.phochak.post.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.post.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.post.adapter.out.persistence.PostFetchCommand;
import com.nexters.phochak.post.adapter.out.persistence.PostSortOption;
import com.nexters.phochak.post.application.port.in.PostFetchDto;
import com.nexters.phochak.post.domain.QPost;
import com.nexters.phochak.shorts.domain.QShorts;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nexters.phochak.report.domain.QReportPost.reportPost;
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
                .join(likes.user).on(likes.user.id.eq(command.getUserId()))
                .join(shorts).on(likes.post.shorts.eq(shorts))
                .where(filterByCursor(command))
                .where(shorts.shortsStateEnum.eq(ShortsStateEnum.OK)) // shorts의 인코딩이 완료된 게시글
                .where(post.id.notIn(
                        JPAExpressions
                                .select(reportPost.post.id)
                                .from(reportPost)
                                .where(reportPost.reporter.id.eq(command.getUserId()))
                )) // 본인이 신고한 게시글 제거
                .limit(command.getPageSize())
                .orderBy(orderByPostSortOption(command.getSortOption())) // 커서 정렬 조건
                .orderBy(post.id.desc())
                .groupBy(post.id)
                .transform(groupBy(post.id).as(
                        new QPostFetchDto(post.id,
                                new QPostFetchDto_PostUserInformation(post.user.id, post.user.nickname, post.user.profileImgUrl),
                                new QPostFetchDto_PostShortsInformation(shorts.id, shorts.shortsStateEnum, shorts.shortsUrl, shorts.thumbnailUrl),
                                likes.post.view, likes.post.postCategory, likes.post.isBlind)
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
