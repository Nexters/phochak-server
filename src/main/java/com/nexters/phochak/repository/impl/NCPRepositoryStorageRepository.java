package com.nexters.phochak.repository.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nexters.phochak.repository.StorageBucketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.Date;

@Repository
@Slf4j
public class NCPRepositoryStorageRepository implements StorageBucketRepository {

    @Value("${ncp.s3.end-point}")
    private String endPoint;
    @Value("${ncp.s3.region-name}")
    private String regionName;
    @Value("${ncp.s3.bucket-name}")
    private String bucketName;
    @Value("${ncp.s3.access-key}")
    private String accessKey;
    @Value("${ncp.s3.secret-key}")
    private String secretKey;

    static AmazonS3 s3Client;

    public NCPRepositoryStorageRepository() {
        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Override
    public URL generatePresignedUrl(String objectName) {
        Date signedUrlExpireSeconds = new Date();
        signedUrlExpireSeconds.setTime(3600);
        return s3Client.generatePresignedUrl(bucketName, objectName, signedUrlExpireSeconds);
    }

    @Override
    public boolean doesExistObject(String objectName) {
        return s3Client.doesObjectExist(bucketName, objectName);
    }
}
