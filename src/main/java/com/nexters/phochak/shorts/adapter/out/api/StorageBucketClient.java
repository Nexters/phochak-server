package com.nexters.phochak.shorts.adapter.out.api;

import java.net.URL;
import java.util.List;

public interface StorageBucketClient {
    URL generatePresignedUrl(String objectName);

    void removeShortsObject(List<String> objectKey);
}
