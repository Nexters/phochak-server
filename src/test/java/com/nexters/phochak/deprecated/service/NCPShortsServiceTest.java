package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.config.property.NCPStorageProperties;
import com.nexters.phochak.notification.application.NotificationService;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.EncodingCallbackRequestDto;
import com.nexters.phochak.shorts.application.NCPShortsService;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import com.nexters.phochak.user.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class NCPShortsServiceTest {

    @InjectMocks NCPShortsService ncpShortsService;
    NCPShortsService mock;

    @Mock
    ShortsRepository shortsRepository;
    @Mock
    NCPStorageProperties ncpStorageProperties;

    @Mock
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        NCPStorageProperties.NCPS3Properties s3 = new NCPStorageProperties.NCPS3Properties("", "", "", "", "", "");
        NCPStorageProperties.NCPShortsProperties shorts = new NCPStorageProperties.NCPShortsProperties("", "", "", "");
        NCPStorageProperties.NCPThumbnailProperties thumbnail = new NCPStorageProperties.NCPThumbnailProperties("", "", "", "");
        ncpStorageProperties = new NCPStorageProperties(s3, shorts, thumbnail);
        mock = new NCPShortsService(shortsRepository, ncpStorageProperties, notificationService);
    }

    @Test
    @DisplayName("인코딩이 끝나있는 경우, 게시글을 shorts 객체와 연결한다")
    void connectShorts_encodingDone() {
        //given
        String uploadKey = "uploadKey";
        Post post = Post.builder()
                .userEntity(new UserEntity())
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
                .userEntity(new UserEntity())
                .postCategory(PostCategoryEnum.TOUR)
                .build();
        given(shortsRepository.findByUploadKey(uploadKey)).willReturn(Optional.empty());

        //when
        mock.connectShorts(uploadKey, post);

        //then
        verify(shortsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("인코딩 진행중 콜백 이후, 포스트 생성이 먼저 끝난 경우에 상태를 변경한다")
    void connectPost_postCreated() {
        //given
        EncodingCallbackRequestDto encodingCallbackRequestDto = EncodingCallbackRequestDto.builder()
                .filePath("/shorts/UPLOADKEY_encoded.mov")
                .status("WAITING")
                .build();
        Shorts shorts = new Shorts();
        given(shortsRepository.findByUploadKey(any())).willReturn(Optional.of(shorts));

        //when
        ncpShortsService.processPost(encodingCallbackRequestDto);

        //then
        assertThat(shorts.getShortsStateEnum()).isEqualTo(ShortsStateEnum.OK);
    }

    @Test
    @DisplayName("인코딩 진행중 콜백 이후, 포스트 생성이 아직 되지 않은 경우에 shorts 객체만 미리 생성한다")
    void processPost_postNotCreated() {
        //given
        EncodingCallbackRequestDto encodingCallbackRequestDto = EncodingCallbackRequestDto.builder()
                .filePath("/shorts/UPLOADKEY_encoded.mov")
                .status("WAITING")
                .build();
        given(shortsRepository.findByUploadKey(any())).willReturn(Optional.empty());

        //when
        mock.processPost(encodingCallbackRequestDto);

        //then
        verify(shortsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("인코딩 완료 콜백 이후, shorts의 상태가 OK로 변경된다")
    void processPost_afterComplete() {
        //given
        EncodingCallbackRequestDto encodingCallbackRequestDto = EncodingCallbackRequestDto.builder()
                .filePath("/shorts/UPLOADKEY_encoded.mov")
                .status("COMPLETE")
                .build();
        doNothing().when(shortsRepository).updateShortState(any(), any());

        //when
        mock.processPost(encodingCallbackRequestDto);

        //then
        verify(shortsRepository, times(1)).updateShortState(any(), eq(ShortsStateEnum.OK));
    }
}
