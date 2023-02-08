package com.nexters.phochak.client.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nexters.phochak.client.StorageBucketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.Date;

@Repository
@Slf4j
public class NCPStorageClient implements StorageBucketClient {

    private final String bucketName;
    private final AmazonS3 s3Client;

    public NCPStorageClient(
            @Value("${ncp.s3.end-point}") String endPoint,
            @Value("${ncp.s3.region-name}") String regionName,
            @Value("${ncp.s3.bucket-name}") String bucketName,
            @Value("${ncp.s3.access-key}") String accessKey,
            @Value("${ncp.s3.secret-key}") String secretKey) {
        this.bucketName = bucketName;
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Override
    public URL generatePresignedUrl(String objectName) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 30; // 30분
        expiration.setTime(expTimeMillis);
        return s3Client.generatePresignedUrl(bucketName, objectName, expiration);
    }

    @Override
    public boolean doesExistObject(String objectName) {
        return s3Client.doesObjectExist(bucketName, objectName);
    }
}
