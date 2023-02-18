package com.nexters.phochak.client;

import java.net.URL;

public interface StorageBucketClient {
    URL generatePresignedUrl(String objectName);

    void removeShortsObject(String objectKey);

    boolean doesExistOriginalObject(String objectName);
}
