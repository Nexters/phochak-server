package com.nexters.phochak.client;

import java.net.URL;

public interface StorageBucketClient {
    URL generatePresignedUrl(String objectName);

    boolean doesExistObject(String objectName);
}
