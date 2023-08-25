package com.nexters.phochak.common.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ncp")
public class NCPStorageProperties {

    private final NCPS3Properties s3;
    private final NCPShortsProperties shorts;
    private final NCPThumbnailProperties thumbnail;

    @Getter
    @RequiredArgsConstructor
    public static class NCPS3Properties {
        private final String endPoint;
        private final String regionName;
        private final String originalBucketName;
        private final String encodedBucketName;
        private final String accessKey;
        private final String secretKey;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NCPShortsProperties {
        private final String fileLocationPrefixHead;
        private final String fileLocationPrefixTail;
        private final String streamingUrlPrefixHead;
        private final String streamingUrlPrefixTail;
    }

    @Getter
    @RequiredArgsConstructor
    public static class NCPThumbnailProperties {
        private final String fileLocationPrefixHead;
        private final String fileLocationPrefixTail;
        private final String thumbnailUrlPrefixHead;
        private final String thumbnailUrlPrefixTail;
    }
}
