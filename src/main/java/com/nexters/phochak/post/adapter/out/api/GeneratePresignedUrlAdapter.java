package com.nexters.phochak.post.adapter.out.api;

import com.nexters.phochak.post.application.port.out.GeneratePresignedUrlPort;
import com.nexters.phochak.shorts.adapter.out.api.StorageBucketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@RequiredArgsConstructor
public class GeneratePresignedUrlAdapter implements GeneratePresignedUrlPort {

    private final StorageBucketClient storageBucketClient;
    @Override
    public URL generate(final String uploadKey, final String fileExtension) {
        final String objectName = uploadKey + "." + fileExtension.toLowerCase();
        return storageBucketClient.generatePresignedUrl(objectName);
    }
}
