package com.nexters.phochak.post.domain;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

@Getter
public class Hashtag {
    public static final int HASHTAG_MAX_SIZE = 20;
    private Long id;
    private Post post;
    private String tag;

    public Hashtag(Post post, String tag) {
        validateConstructor(post, tag);
        this.post = post;
        this.tag = tag;
    }

    private static void validateConstructor(final Post post, final String tag) {
        Assert.notNull(post, "post must not be null");
        Assert.notNull(tag, "tag must not be null");
        if (tag.length() > HASHTAG_MAX_SIZE) {
            throw new IllegalArgumentException("tag must not be longer than " + HASHTAG_MAX_SIZE);
        }
        validateHashtagString(tag);
    }

    private static void validateHashtagString(final String tag) {
        final Pattern pattern = Pattern.compile("[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_]{1,20}$");
        if (!pattern.matcher(tag).matches()) {
            throw new PhochakException(ResCode.INVALID_INPUT, "해시태그 형식이 올바르지 않습니다.");
        }
    }
}
