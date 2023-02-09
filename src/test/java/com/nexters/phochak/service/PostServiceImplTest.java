package com.nexters.phochak.service;

import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.client.StorageBucketClient;
import com.nexters.phochak.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks PostServiceImpl postService;

    @Mock
    StorageBucketClient storageBucketClient;

    @Test
    @DisplayName("Presigned URL과 UploadKey 생성에 성공한다")
    void generateUploadKey() throws MalformedURLException {
        //given
        given(storageBucketClient.generatePresignedUrl(any())).willReturn(new URL("http://test.com"));

        //when
        PostUploadKeyResponseDto result = postService.generateUploadKey("mov");

        //then
        assertThat(result.getUploadKey()).isNotNull();
    }

}