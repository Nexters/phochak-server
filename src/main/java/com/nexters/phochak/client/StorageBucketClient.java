package com.nexters.phochak.client;

import java.net.URL;
import java.util.List;

public interface StorageBucketClient {
    URL generatePresignedUrl(String objectName);

    void removeShortsObject(List<String> objectKey);

    boolean doesExistOriginalObject(String objectName);
}
