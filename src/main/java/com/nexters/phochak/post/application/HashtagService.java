package com.nexters.phochak.post.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.HashtagEntity;
import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostMapper;
import com.nexters.phochak.post.application.port.in.HashtagUseCase;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class HashtagService implements HashtagUseCase {

    private final HashtagRepository hashtagRepository;
    private final PostMapper postMapper;

    @Override
    public List<HashtagEntity> saveHashtags(Post post, List<String> stringHashtagList) {
        if (stringHashtagList.isEmpty()) {
            return Collections.emptyList();
        }
        validateHashtag(stringHashtagList);
        List<HashtagEntity> hashtagEntityList = stringHashtagList.stream().map(stringHashtag ->
                HashtagEntity.builder()
                        .post(postMapper.toEntity(post))
                        .tag(stringHashtag)
                        .build()
        ).toList();
        return hashtagRepository.saveAll(hashtagEntityList);
    }

    @Override
    public Map<Long, HashtagFetchDto> findHashtagsOfPosts(List<Long> postIds) {
        return hashtagRepository.findHashTagsOfPost(postIds);
    }

    @Override
    public void updateAll(Post post, List<String> stringHashtagList) {
        hashtagRepository.deleteAllByPostId(post.getId());
        saveHashtags(post, stringHashtagList);
    }

    //TODO: 도메인 로직으로 분리
    private static void validateHashtag(List<String> stringHashtagList) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_]{1,20}$");
        for(String tag : stringHashtagList) {
            if (!pattern.matcher(tag).matches()) {
                throw new PhochakException(ResCode.INVALID_INPUT, "해시태그 형식이 올바르지 않습니다.");
            }
        }
    }

}
