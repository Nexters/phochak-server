package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.PostFetchDto;
import com.nexters.phochak.post.application.port.in.QPostFetchDto;
import com.nexters.phochak.post.application.port.in.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.post.application.port.in.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nexters.phochak.ignore.adapter.out.persistence.QIgnoredUserEntity.ignoredUserEntity;
import static com.nexters.phochak.post.adapter.out.persistence.QPostEntity.postEntity;
import static com.nexters.phochak.report.domain.QReportPost.reportPost;
import static com.querydsl.core.group.GroupBy.groupBy;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostFetchDto> findNextPageByCommmand(PostFetchCommand command) {
        Map<Long, PostFetchDto> resultMap = queryFactory.from(postEntity)
                .join(postEntity.user)
                .join(postEntity.shorts)
                .where(filterByCursor(command)) // 커서 기반 페이징
                .where(getFilterExpression(command)) // 내가 업로드한 게시글
                .where(postEntity.user.id.notIn(
                        JPAExpressions
                                .select(ignoredUserEntity.ignoredUserRelation.ignoredUser.id)
                                .from(ignoredUserEntity)
                                .where(ignoredUserEntity.ignoredUserRelation.user.id.eq(command.getUserId()))
                )) //본인이 ignore한 게시글 제거
                .where(postEntity.id.notIn(
                        JPAExpressions
                                .select(reportPost.post.id)
                                .from(reportPost)
                                .where(reportPost.reporter.id.eq(command.getUserId()))
                )) // 본인이 신고한 게시글 제거
                .where(postEntity.shorts.shortsStateEnum.eq(ShortsStateEnum.OK)) // shorts의 인코딩이 완료된 게시글
                .limit(command.getPageSize())
                .orderBy(orderByPostSortOption(command.getSortOption())) // 커서 정렬 조건
                .orderBy(postEntity.id.desc())
                .transform(groupBy(postEntity.id)
                        .as(new QPostFetchDto(postEntity.id,
                                new QPostFetchDto_PostUserInformation(postEntity.user.id, postEntity.user.nickname, postEntity.user.profileImgUrl),
                                new QPostFetchDto_PostShortsInformation(postEntity.shorts.id, postEntity.shorts.shortsStateEnum, postEntity.shorts.shortsUrl, postEntity.shorts.thumbnailUrl),
                                postEntity.view,
                                postEntity.postCategory,
                                postEntity.isBlind
                        )));

        return resultMap.keySet().stream()
                .map(resultMap::get)
                .collect(Collectors.toList());
    }

    private static BooleanExpression getFilterExpression(PostFetchCommand command) {
        if (command.hasUploadedFilter()) {
            if(command.getTargetUserId() == null) {
                return postEntity.user.id.eq(command.getUserId());
            } else {
                return postEntity.user.id.eq(command.getTargetUserId());
            }
        }
        return null;
    }

    private BooleanExpression filterByCursor(PostFetchCommand command) {
        BooleanExpression defaultFilter = postEntity.id.lt(command.getLastId());
        switch (command.getSortOption()) {
            case VIEW:
                return postEntity.view.loe(command.getSortValue()).and(defaultFilter);
            case LIKE:
                return postEntity.likes.size().loe(command.getSortValue()).and(defaultFilter);
            default:
                return defaultFilter;
        }
    }

    private static OrderSpecifier orderByPostSortOption(PostSortOption postSortOption) {
        if (postSortOption == PostSortOption.LIKE) {
            return postEntity.likes.size().desc();
        } else if (postSortOption == PostSortOption.VIEW) {
            return postEntity.view.desc();
        }
        return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }
}
