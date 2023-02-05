package com.nexters.phochak.specification;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;

public enum PostSortCriteria {
    LATEST, PHOCHAK, VIEW;

    public static PostSortCriteria nameOf(String name) {
        for (PostSortCriteria target : PostSortCriteria.values()) {
            if (target.name().equalsIgnoreCase(name)) {
                return target;
            }
        }

        throw new PhochakException(ResCode.NOT_SUPPROTED_CRITERIA);
    }
}
