package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Hashtag;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
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
    public List<Hashtag> createHashtagsByString(List<String> stringHashtagList, Post post) {
        List<Hashtag> hashtagList = stringHashtagList.stream().map(stringHashtag -> {
                    validateHashtag(stringHashtag);
                    return Hashtag.builder()
                        .post(post)
                        .tag(stringHashtag)
                        .build();
                }
        ).collect(Collectors.toList());
        return hashtagRepository.saveAll(hashtagList);
    }

    private void validateHashtag(String tag) {
        if(tag.contains(" ")) {
            throw new PhochakException(ResCode.INVALID_INPUT, " 해시태그에 공백이 존재합니다.");
        }
    }

}
