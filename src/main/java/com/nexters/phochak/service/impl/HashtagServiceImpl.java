package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Hashtag;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.repository.HashtagRepository;
import com.nexters.phochak.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;

    @Override
    public void createHashtagsByString(List<String> stringHashtagList, Post post) {
        List<Hashtag> hashtagList = stringHashtagList.stream().map(stringHashtag ->
                Hashtag.builder()
                        .post(post)
                        .tag(stringHashtag)
                        .build()
        ).collect(Collectors.toList());
        hashtagRepository.saveAll(hashtagList);
    }

}
