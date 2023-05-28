package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;
import com.nexters.phochak.dto.QPostFetchDto;
import com.nexters.phochak.dto.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.dto.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.repository.PostCustomRepository;
import com.nexters.phochak.specification.PostSortOption;
import com.nexters.phochak.specification.ShortsStateEnum;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nexters.phochak.domain.QReportPost.reportPost;
import static com.querydsl.core.group.GroupBy.groupBy;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private static final QPost post = QPost.post;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostFetchDto> findNextPageByCommmand(PostFetchCommand command) {
        Map<Long, PostFetchDto> resultMap = queryFactory.from(post)
                .join(post.user)
                .join(post.shorts)
                .where(filterByCursor(command)) // 커서 기반 페이징
                .where(getFilterExpression(command)) // 내가 업로드한 게시글
                .where(post.id.notIn(
                        JPAExpressions
                                .select(reportPost.post.id)
                                .from(reportPost)
                                .where(reportPost.reporter.id.eq(command.getUserId()))
                )) // 본인이 신고한 게시글 제거
                .where(post.shorts.shortsStateEnum.eq(ShortsStateEnum.OK)) // shorts의 인코딩이 완료된 게시글
                .limit(command.getPageSize())
                .orderBy(orderByPostSortOption(command.getSortOption())) // 커서 정렬 조건
                .orderBy(post.id.desc())
                .transform(groupBy(post.id)
                        .as(new QPostFetchDto(post.id,
                                new QPostFetchDto_PostUserInformation(post.user.id, post.user.nickname, post.user.profileImgUrl),
                                new QPostFetchDto_PostShortsInformation(post.shorts.id, post.shorts.shortsStateEnum, post.shorts.shortsUrl, post.shorts.thumbnailUrl),
                                post.view,
                                post.postCategory,
                                post.isBlind
                        )));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(Collectors.toList());
    }

    private static BooleanExpression getFilterExpression(PostFetchCommand command) {
        if (command.hasUploadedFilter()) {
            return post.user.id.eq(command.getUserId());
        }
        return null;
    }

    private BooleanExpression filterByCursor(PostFetchCommand command) {
        BooleanExpression defaultFilter = post.id.lt(command.getLastId());
        switch (command.getSortOption()) {
            case VIEW:
                return post.view.loe(command.getSortValue()).and(defaultFilter);
            case LIKE:
                return post.likes.size().loe(command.getSortValue()).and(defaultFilter);
            default:
                return defaultFilter;
        }
    }

    private static OrderSpecifier orderByPostSortOption(PostSortOption postSortOption) {
        if (postSortOption == PostSortOption.LIKE) {
            return post.likes.size().desc();
        } else if (postSortOption == PostSortOption.VIEW) {
            return post.view.desc();
        }
        return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }
}
