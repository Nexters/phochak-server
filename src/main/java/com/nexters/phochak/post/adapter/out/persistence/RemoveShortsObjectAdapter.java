package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.post.application.port.out.RemoveShortsObjectPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.presentation.StorageBucketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveShortsObjectAdapter implements RemoveShortsObjectPort {

    private StorageBucketClient storageBucketClient;

    @Override
    public void remove(final List<Post> postList) {
        List<String> objectKeyList = postList.stream()
                .map(post -> post.getShorts().getUploadKey())
                .toList();
        storageBucketClient.removeShortsObject(objectKeyList);
    }
}
