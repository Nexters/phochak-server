package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.DeleteShortsPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.adapter.out.persistence.ShortsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteShortsAdapter implements DeleteShortsPort {

    private final ShortsRepository shortsRepository;

    @Override
    public void deleteAllIn(final List<Post> postList) {
        final List<String> uploadKeyList = postList.stream().map(post -> post.getShortsEntity().getUploadKey()).toList();
        shortsRepository.deleteAllByUploadKeyIn(uploadKeyList);
    }
}
