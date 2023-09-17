package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateViewAdapter implements UpdateViewPort {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public void increaseView(final Long postId) {
        final int countOfUpdatedRow = postRepository.updateView(postId);
        if (countOfUpdatedRow < 1) {
            throw new PhochakException(ResCode.NOT_FOUND_POST);
        }
    }
}
