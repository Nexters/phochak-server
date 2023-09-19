package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.DeleteHashtagPort;
import com.nexters.phochak.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteHashtagAdapter implements DeleteHashtagPort {

    private final HashtagRepository hashtagRepository;

    @Override
    public void deleteAllByPost(final Post post) {
        hashtagRepository.deleteAllByPostId(post.getId());
    }

    @Override
    public void deleteAllByPostIdIn(final List<Post> postList) {
        final List<Long> postIdList = postList.stream().map(Post::getId).toList();
        hashtagRepository.deleteAllByPostIdIn(postIdList);
    }
}
