package com.nexters.phochak.post.domain;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;

public enum PostSortOption {
    LATEST, LIKE, VIEW;

    public static PostSortOption nameOf(String name) {
        for (PostSortOption target : PostSortOption.values()) {
            if (target.name().equalsIgnoreCase(name)) {
                return target;
            }
        }

        throw new PhochakException(ResCode.NOT_SUPPORTED_SORT_OPTION);
    }
}
