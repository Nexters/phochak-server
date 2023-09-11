package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.in.PostFetchDto;
import com.nexters.phochak.post.application.port.in.QPostFetchDto;
import com.nexters.phochak.post.application.port.in.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.post.application.port.in.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nexters.phochak.post.adapter.out.persistence.QHashtag.hashtag;
import static com.nexters.phochak.post.adapter.out.persistence.QPostEntity.postEntity;
import static com.nexters.phochak.post.adapter.out.persistence.QReportPost.reportPost;
import static com.nexters.phochak.user.adapter.out.persistence.QIgnoredUserEntity.ignoredUserEntity;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds) {
        return queryFactory.from(hashtag)
                .where(hashtag.post.id.in(postIds))
                .transform(groupBy(hashtag.post.id)
                        .as(new QHashtagFetchDto(list(hashtag.tag))));
    }

    @Override
    public List<PostFetchDto> findSearchedPageByCommmand(PostFetchCommand command) {
        Map<Long, PostFetchDto> resultMap = queryFactory.from(postEntity)
                .join(postEntity.user)
                .join(postEntity.shorts)
                .where(filterByCursor(command)) // 커서 기반 페이징
                .where(filterByCategory(command.getCategory()))
                .where(searchByHashtag(command.getSearchHashtag()))
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

    private BooleanExpression searchByHashtag(String searchHashtag) {
        if(searchHashtag == null) {
            return null;
        }
        return postEntity.hashtags.any().tag.eq(searchHashtag);
    }

    private BooleanExpression filterByCategory(PostCategoryEnum category) {
        if(category == null) {
            return null;
        }
        return postEntity.postCategory.eq(category);
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
}
