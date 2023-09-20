package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.adapter.out.api.StorageBucketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteMediaAdapter implements DeleteMediaPort {

    private final StorageBucketClient storageBucketClient;

    @Override
    public void deleteShortsMedia(final Post post) {
        String objectKey = post.getShortsEntity().getUploadKey();
        storageBucketClient.removeShortsObject(List.of(objectKey));
    }
}
