package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.GetHashtagAutocompletePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetHashtagAutocompleteAdapter implements GetHashtagAutocompletePort {

    private final HashtagRepository hashtagRepository;

    @Override
    public List<String> search(final String hashtag, final int resultSize) {
        final Pageable pageable = PageRequest.of(0, resultSize);
        return hashtagRepository.findByHashtagStartsWith(hashtag, pageable);
    }
}
