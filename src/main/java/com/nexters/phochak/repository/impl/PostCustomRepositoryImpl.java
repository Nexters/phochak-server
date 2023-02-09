package com.nexters.phochak.repository.impl;

import com.nexters.phochak.auth.UserContext;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.dto.request.CustomCursor;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private static final int ID_PADDING = 19;
    private static final int CRITERIA_PADDING = 10;
    public static final char ZERO = '0';
    private static final QPost post = QPost.post;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostPageResponseDto> findNextPageByCursor(CustomCursor customCursor) {
        List<Post> result = queryFactory.selectFrom(post)
                .join(post.user).fetchJoin()
                .join(post.shorts).fetchJoin()
                .leftJoin(post.likes).fetchJoin()
                .leftJoin(post.hashtags)
                .where(filterByCursor(customCursor.getSortOption(), customCursor))
                .limit(customCursor.getPageSize())
                .orderBy(orderByPostSortOption(customCursor.getSortOption()))
                .orderBy(orderById(customCursor.getSortOption()))
                .fetch();

        final Long userId = UserContext.CONTEXT.get();
        log.debug("findNextPageByCursor: {}", userId);

        return result.stream()
                .map(p -> PostPageResponseDto.from(p, userId))
                .collect(Collectors.toList());
    }

    private BooleanExpression filterByCursor(PostSortOption postSortOption, CustomCursor cursor) {
        String cursorString = cursor.createCursorString();
        if (postSortOption == PostSortOption.LATEST) {
            return post.id.lt(Long.valueOf(cursorString.substring(CRITERIA_PADDING)));
        } else if (postSortOption == PostSortOption.VIEW) {
            return StringExpressions.lpad(post.view.stringValue(), CRITERIA_PADDING, ZERO)
                    .concat(StringExpressions.lpad(post.id.stringValue(), ID_PADDING, ZERO))
                    .lt(cursorString);
        } else if (postSortOption == PostSortOption.LIKE) {
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

    private static OrderSpecifier<Long> orderById(PostSortOption postSortOption) {
        return postSortOption == PostSortOption.LATEST ? post.id.desc() : post.id.asc();
    }
}
