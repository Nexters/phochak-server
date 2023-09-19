package com.nexters.phochak.post.application;

import com.nexters.phochak.post.adapter.out.persistence.HashtagFetchDto;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.application.port.in.HashtagUseCase;
import com.nexters.phochak.post.application.port.out.SaveHashtagPort;
import com.nexters.phochak.post.domain.Hashtag;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HashtagService implements HashtagUseCase {

    private final HashtagRepository hashtagRepository;
    private final SaveHashtagPort saveHashtagPort;

    @Override
    public void saveHashtags(Post post, List<String> stringHashtagList) {
        List<Hashtag> hashtagList = stringHashtagList.stream()
                .map(stringHashtag -> new Hashtag(post, stringHashtag)).toList();
        saveHashtagPort.saveAll(hashtagList);
        post.setHashtagList(hashtagList);
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

}
