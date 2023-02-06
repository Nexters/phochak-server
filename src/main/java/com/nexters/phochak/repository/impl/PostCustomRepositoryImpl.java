package com.nexters.phochak.repository.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.QPost;
import com.nexters.phochak.dto.request.CustomCursor;
import com.nexters.phochak.dto.response.PostPageResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostCustomRepository;
import com.nexters.phochak.specification.PostSortCriteria;
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
                .join(post.phochaks).fetchJoin()
                .where(filterByCursor(customCursor.getPostSortCriteria(), customCursor))
                .limit(customCursor.getPageSize())
                .orderBy(orderByPostSortCriteria(customCursor.getPostSortCriteria()))
                .orderBy(orderById(customCursor.getPostSortCriteria()))
                .fetch();

        return result.stream()
                .map(PostPageResponseDto::from)
                .collect(Collectors.toList());
    }

    private BooleanExpression filterByCursor(PostSortCriteria postSortCriteria, CustomCursor cursor) {
        String cursorString = cursor.createCursorString();
        if (postSortCriteria == PostSortCriteria.LATEST) {
            return post.id.lt(Long.valueOf(cursorString.substring(CRITERIA_PADDING)));
        } else if (postSortCriteria == PostSortCriteria.VIEW) {
            return StringExpressions.lpad(post.view.stringValue(), CRITERIA_PADDING, ZERO)
                    .concat(StringExpressions.lpad(post.id.stringValue(), ID_PADDING, ZERO))
                    .lt(cursorString);
        } else if (postSortCriteria == PostSortCriteria.PHOCHAK) {
            return StringExpressions.lpad(post.phochaks.size().stringValue(), CRITERIA_PADDING, ZERO)
                    .concat(StringExpressions.lpad(post.id.stringValue(), ID_PADDING, ZERO))
                    .lt(cursorString);
        }
        throw new PhochakException(ResCode.NOT_SUPPROTED_CRITERIA);
    }

    private static OrderSpecifier orderByPostSortCriteria(PostSortCriteria postSortCriteria) {
        if (postSortCriteria == PostSortCriteria.PHOCHAK) {
            return post.phochaks.size().desc();
        } else if (postSortCriteria == PostSortCriteria.VIEW) {
            return post.view.desc();
        }
        return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, OrderSpecifier.NullHandling.Default);
    }

    private static OrderSpecifier<Long> orderById(PostSortCriteria postSortCriteria) {
        return postSortCriteria == PostSortCriteria.LATEST ? post.id.desc() : post.id.asc();
    }
}
