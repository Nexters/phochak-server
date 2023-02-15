package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostCustomRepository;
import com.nexters.phochak.specification.PostSortOption;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private static final int ID_PADDING = 19;
    private static final int CRITERIA_PADDING = 10;
    public static final char ZERO = '0';
    private static final QPost post = QPost.post;

    private final JPAQueryFactory queryFactory;
    private BooleanExpression filter;

    @Override
    public List<PostPageResponseDto> findNextPageByCursor(PostFetchCommand command) {
        List<Post> result = queryFactory.selectFrom(post)
                .join(post.user).fetchJoin()
                .join(post.shorts).fetchJoin()
                .leftJoin(post.likes).fetchJoin()
                .leftJoin(post.hashtags)
                .where(filterByCursor(command)) // 커서 기반 페이징
                .where(getFilterExpression(command)) // 내가 업로드하거나 좋아요한 게시글
                .limit(command.getPageSize())
                .orderBy(orderByPostSortOption(command.getSortOption())) // 커서 정렬 조건
                .orderBy(post.id.desc())
                .fetch();

        return result.stream()
                .map(p -> PostPageResponseDto.from(p, command.getUserId()))
                .collect(Collectors.toList());
    }

    private static BooleanExpression getFilterExpression(PostFetchCommand command) {
        if (Objects.isNull(command.getFilter())) {
            return null;
        }

        if (command.hasUploadedFilter()) {
            return post.user.id.eq(command.getUserId());
        }
        // TODO: 내가 좋아요한 게시글
        return null;
    }

    private BooleanExpression filterByCursor(PostFetchCommand command) {
        String cursorString = command.createCursorString();
        switch (command.getSortOption()) {
            case LATEST:
                return post.id.lt(command.getLastId());
            case VIEW:
                return StringExpressions.lpad(post.view.stringValue(), CRITERIA_PADDING, ZERO)
                        .concat(StringExpressions.lpad(post.id.stringValue(), ID_PADDING, ZERO))
                        .lt(cursorString);
            case LIKE:
                return StringExpressions.lpad(post.likes.size().stringValue(), CRITERIA_PADDING, ZERO)
                        .concat(StringExpressions.lpad(post.id.stringValue(), ID_PADDING, ZERO))
                        .lt(cursorString);
        }
        throw new PhochakException(ResCode.NOT_SUPPORTED_SORT_OPTION);
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
