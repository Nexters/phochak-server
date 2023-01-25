package com.nexters.phochak.specification;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import lombok.Getter;

@Getter
public enum PostCategoryEnum {
    RESTAURANT("RESTAURANT"), TOUR("TOUR");

    private final String name;

    PostCategoryEnum(String name) {
        this.name = name;
    }

    public static PostCategoryEnum nameOf(String name) {
        for (PostCategoryEnum target : PostCategoryEnum.values()) {
            if (target.getName().equals(name)) {
                return target;
            }
        }
        throw new PhochakException(ResCode.INVALID_INPUT, "올바르지 않은 게시글 카테고리 입니다.");
    }
}
