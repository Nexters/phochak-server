package com.nexters.phochak.specification;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;

public enum PostSortCriteria {
    LATEST, PHOCHAK, HIT;

    public static PostSortCriteria nameOf(String name) {
        for (PostSortCriteria target : PostSortCriteria.values()) {
            if (target.name().equalsIgnoreCase(name)) {
                return target;
            }
        }

        throw new PhochakException(ResCode.INVALID_INPUT, "지원하지 않는 Post 정렬 기준입니다.");
    }
}
