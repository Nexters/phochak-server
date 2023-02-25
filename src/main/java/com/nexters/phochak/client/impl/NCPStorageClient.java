package com.nexters.phochak.client.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nexters.phochak.client.StorageBucketClient;
import com.nexters.phochak.config.property.NCPStorageProperties;
import lombok.extern.slf4j.Slf4j;
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

    public NCPStorageClient(NCPStorageProperties ncpStorageProperties) {
        this.originalBucketName = ncpStorageProperties.getS3().getOriginalBucketName();
        this.encodedBucketName = ncpStorageProperties.getS3().getEncodedBucketName();
        this.shortsLocationPrefixHead = ncpStorageProperties.getShorts().getFileLocationPrefixHead();
        this.shortsLocationPrefixTail = ncpStorageProperties.getShorts().getFileLocationPrefixTail();
        this.thumbnailLocationPrefixHead = ncpStorageProperties.getThumbnail().getFileLocationPrefixHead();
        this.thumbnailLocationPrefixTail = ncpStorageProperties.getThumbnail().getFileLocationPrefixTail();
        String endPoint = ncpStorageProperties.getS3().getEndPoint();
        String regionName = ncpStorageProperties.getS3().getRegionName();
        String accessKey = ncpStorageProperties.getS3().getAccessKey();
        String secretKey = ncpStorageProperties.getS3().getSecretKey();
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
