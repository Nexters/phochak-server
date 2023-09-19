package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.adapter.out.persistence.HashtagEntity;
import com.nexters.phochak.post.adapter.out.persistence.HashtagMapper;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.domain.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveHashtagAdapter implements SaveHashtagPort {

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;

    @Override
    public void saveAll(final List<Hashtag> hashtagList) {
        final List<HashtagEntity> hashtagEntityList = hashtagList.stream().map(hashtagMapper::toEntity).toList();
        hashtagRepository.saveAll(hashtagEntityList);
    }
}
