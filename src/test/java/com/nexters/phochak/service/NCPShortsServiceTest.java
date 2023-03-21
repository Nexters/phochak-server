package com.nexters.phochak.service;

import com.nexters.phochak.config.property.NCPStorageProperties;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.service.impl.NCPShortsService;
import com.nexters.phochak.specification.PostCategoryEnum;
import com.nexters.phochak.specification.ShortsStateEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NCPShortsServiceTest {

    @InjectMocks NCPShortsService ncpShortsService;
    NCPShortsService mock;

    @Mock
    ShortsRepository shortsRepository;
    @Mock
    NCPStorageProperties ncpStorageProperties;

    @BeforeEach
    void setUp() {
        NCPStorageProperties.NCPS3Properties s3 = new NCPStorageProperties.NCPS3Properties("", "", "", "", "", "");
        NCPStorageProperties.NCPShortsProperties shorts = new NCPStorageProperties.NCPShortsProperties("", "", "", "");
        NCPStorageProperties.NCPThumbnailProperties thumbnail = new NCPStorageProperties.NCPThumbnailProperties("", "", "", "");
        ncpStorageProperties = new NCPStorageProperties(s3, shorts, thumbnail);
        mock = new NCPShortsService(shortsRepository, ncpStorageProperties);
    }

    @Test
    @DisplayName("인코딩이 끝나있는 경우, 게시글을 shorts 객체와 연결한다")
    void connectShorts_encodingDone() {
        //given
        String uploadKey = "uploadKey";
        Post post = Post.builder()
                .user(new User())
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        Shorts shorts = Shorts.builder()
                .shortsUrl("url")
                .thumbnailUrl("url")
                .build();
        given(shortsRepository.findByUploadKey(uploadKey)).willReturn(Optional.of(shorts));

        //when
        ncpShortsService.connectShorts(uploadKey, post);

        //then
        assertThat(post.getShorts()).isEqualTo(shorts);
        assertThat(shorts.getShortsStateEnum()).isEqualTo(ShortsStateEnum.OK);
    }

    @Test
    @DisplayName("인코딩이 진행중인 경우, ShortsStatus를 in progress로 유지하고 shorts 객체를 생성한다")
    void connectShorts_encodingInProgress() {
        //given
        String uploadKey = "uploadKey";
        Post post = Post.builder()
                .user(new User())
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        given(shortsRepository.findByUploadKey(uploadKey)).willReturn(Optional.empty());

        //when
        mock.connectShorts(uploadKey, post);

        //then
        verify(shortsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("인코딩 완료 콜백 이후, 포스트 생성이 먼저 끝난 경우에 상태를 변경한다")
    void connectPost_postCreated() {
        //given
        EncodingCallbackRequestDto encodingCallbackRequestDto = EncodingCallbackRequestDto.builder()
                .filePath("/shorts/UPLOADKEY_encoded.mov")
                .build();
        Post post = Post.builder()
                .user(new User())
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        Shorts shorts = new Shorts();
        given(shortsRepository.findByUploadKey(any())).willReturn(Optional.of(shorts));

        //when
        ncpShortsService.connectPost(encodingCallbackRequestDto.getFilePath());

        //then
        assertThat(shorts.getShortsStateEnum()).isEqualTo(ShortsStateEnum.OK);
    }

    @Test
    @DisplayName("인코딩 완료 콜백 이후, 포스트 생성이 아직 되지 않은 경우에 shorts 객체만 미리 생성한다")
    void connectPost_postNotCreated() {
        //given
        EncodingCallbackRequestDto encodingCallbackRequestDto = EncodingCallbackRequestDto.builder()
                .filePath("/shorts/UPLOADKEY_encoded.mov")
                .build();
        given(shortsRepository.findByUploadKey(any())).willReturn(Optional.empty());

        //when
        mock.connectPost(encodingCallbackRequestDto.getFilePath());

        //then
        verify(shortsRepository, times(1)).save(any());
    }
}
