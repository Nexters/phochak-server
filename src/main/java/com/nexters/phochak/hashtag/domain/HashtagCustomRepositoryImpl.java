package com.nexters.phochak.hashtag.domain;

import com.nexters.phochak.post.PostFetchCommand;
import com.nexters.phochak.post.PostFetchDto;
import com.nexters.phochak.post.QPostFetchDto;
import com.nexters.phochak.post.QPostFetchDto_PostShortsInformation;
import com.nexters.phochak.post.QPostFetchDto_PostUserInformation;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.post.domain.QPost;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nexters.phochak.ignore.domain.QIgnoredUsers.ignoredUsers;
import static com.nexters.phochak.report.domain.QReportPost.reportPost;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {

    private final JPAQueryFactory queryFactory;
    private static final QHashtag hashtag = QHashtag.hashtag;
    private static final QPost post = QPost.post;

    @Override
    public Map<Long, HashtagFetchDto> findHashTagsOfPost(List<Long> postIds) {
        return queryFactory.from(hashtag)
                .where(hashtag.post.id.in(postIds))
                .transform(groupBy(hashtag.post.id)
                        .as(new QHashtagFetchDto(list(hashtag.tag))));
    }

    @Override
    public List<PostFetchDto> findSearchedPageByCommmand(PostFetchCommand command) {
        Map<Long, PostFetchDto> resultMap = queryFactory.from(post)
                .join(post.user)
                .join(post.shorts)
                .where(filterByCursor(command)) // 커서 기반 페이징
                .where(filterByCategory(command.getCategory()))
                .where(searchByHashtag(command.getSearchHashtag()))
                .where(post.user.id.notIn(
                        JPAExpressions
                                .select(ignoredUsers.ignoredUsersRelation.ignoredUser.id)
                                .from(ignoredUsers)
                                .where(ignoredUsers.ignoredUsersRelation.user.id.eq(command.getUserId()))
                )) //본인이 ignore한 게시글 제거
                .where(post.id.notIn(
                        JPAExpressions
                                .select(reportPost.post.id)
                                .from(reportPost)
                                .where(reportPost.reporter.id.eq(command.getUserId()))
                )) // 본인이 신고한 게시글 제거
                .where(post.shorts.shortsStateEnum.eq(ShortsStateEnum.OK)) // shorts의 인코딩이 완료된 게시글
                .limit(command.getPageSize())
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

    private BooleanExpression searchByHashtag(String searchHashtag) {
        if(searchHashtag == null) {
            return null;
        }
        return post.hashtags.any().tag.eq(searchHashtag);
    }

    private BooleanExpression filterByCategory(PostCategoryEnum category) {
        if(category == null) {
            return null;
        }
        return post.postCategory.eq(category);
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
}
