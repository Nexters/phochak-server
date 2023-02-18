package com.nexters.phochak.client.impl;

import com.amazonaws.HttpMethod;
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

    private final String encodedBucketName;
    private final String originalBucketName;
    private final AmazonS3 s3Client;
    private final String shortsLocationPrefixHead;
    private final String shortsLocationPrefixTail;
    private final String thumbnailLocationPrefixHead;
    private final String thumbnailLocationPrefixTail;

    public NCPStorageClient(
            @Value("${ncp.s3.end-point}") String endPoint,
            @Value("${ncp.s3.region-name}") String regionName,
            @Value("${ncp.s3.original-bucket-name}") String originalBucketName,
            @Value("${ncp.s3.encoded-bucket-name}") String encodedBucketName,
            @Value("${ncp.s3.access-key}") String accessKey,
            @Value("${ncp.s3.secret-key}") String secretKey,
            @Value("${ncp.shorts.file-location-prefix.head}") String shortsLocationPrefixHead,
            @Value("${ncp.shorts.file-location-prefix.tail}") String shortsLocationPrefixTail,
            @Value("${ncp.thumbnail.file-location-prefix.head}") String thumbnailLocationPrefixHead,
            @Value("${ncp.thumbnail.file-location-prefix.tail}") String thumbnailLocationPrefixTail
            ) {
        this.originalBucketName = originalBucketName;
        this.encodedBucketName = encodedBucketName;
        this.shortsLocationPrefixHead = shortsLocationPrefixHead;
        this.shortsLocationPrefixTail = shortsLocationPrefixTail;
        this.thumbnailLocationPrefixHead = thumbnailLocationPrefixHead;
        this.thumbnailLocationPrefixTail = thumbnailLocationPrefixTail;
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
        return s3Client.generatePresignedUrl(originalBucketName, objectName, expiration, HttpMethod.PUT);
    }

    @Override
    public void removeShortsObject(String objectKey) {
        //TODO: 비동기 처리
        s3Client.deleteObject(encodedBucketName, shortsLocationPrefixHead + objectKey + shortsLocationPrefixTail);
        s3Client.deleteObject(encodedBucketName, thumbnailLocationPrefixHead + objectKey+ thumbnailLocationPrefixTail);
    }

    @Override
    public boolean doesExistOriginalObject(String objectName) {
        return s3Client.doesObjectExist(originalBucketName, objectName);
    }
}
