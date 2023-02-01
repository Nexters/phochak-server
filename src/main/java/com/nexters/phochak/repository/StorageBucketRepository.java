package com.nexters.phochak.repository;

import com.amazonaws.services.s3.AmazonS3;

import java.net.URL;

public interface StorageBucketRepository {
    URL generatePresignedUrl(String objectName);

    boolean doesExistObject(String objectName);
}
