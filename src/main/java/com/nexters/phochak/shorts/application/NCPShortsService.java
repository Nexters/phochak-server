package com.nexters.phochak.shorts.application;

import com.nexters.phochak.common.config.property.NCPStorageProperties;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.shorts.application.port.in.EncodingCallbackRequestDto;
import com.nexters.phochak.shorts.application.port.in.ShortsUseCase;
import com.nexters.phochak.shorts.application.port.out.LoadShortsPort;
import com.nexters.phochak.shorts.application.port.out.NotifyEncodingStatePort;
import com.nexters.phochak.shorts.application.port.out.SaveShortsPort;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.shorts.domain.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NCPShortsService implements ShortsUseCase {

//    private final ShortsRepository shortsRepository;
    private final LoadShortsPort loadShortsPort;
    private final SaveShortsPort saveShortsPort;
    private final NotifyEncodingStatePort notifyEncodingStatePort;
    private final NCPStorageProperties ncpStorageProperties;

    /**
     * 인코딩 성공 순서와 게시글 생성의 순서는 모듈 상태에 따라서 달라질 수 있습니다.
     * 인코딩이 먼저 끝나는 경우: 인코딩 콜백이 먼저 들어온 경우입니다. shorts 객체를 생성합니다.
     * 게시글이 먼저 생성되는 경우: 일반적인 케이스입니다. 상태값만 변경합니다.
     */
    @Override
    public void connectShorts(Post post, String uploadKey) {
        Shorts shorts = loadShortsPort.findByUploadKey(uploadKey);
        if (shorts != null) {
            shorts.updateShortsState(ShortsStateEnum.OK);
        } else {
            String shortsFileName = generateShortsFileName(uploadKey);
            String thumbnailFileName = generateThumbnailsFileName(uploadKey);
            shorts = new Shorts(ShortsStateEnum.IN_PROGRESS, uploadKey, shortsFileName, thumbnailFileName);
        }
        saveShortsPort.save(shorts);
        post.setShorts(shorts);
    }

    /**
     * 인코딩 콜백은 다음 순서로 들어옵니다.
     * 성공 시: WAITING - RUNNING - COMPLETE
     * 성공 시: WAITING - RUNNING - FAILURE
     * 인코딩 콜백 수신 시 다음과 같은 절차를 수행합니다.
     * 1. Shorts 객체 Post 객체와 연결
     * 2. Shorts 상태 변경
     * 3. 각 상태에 대한 푸시 알람 발송
     */
    @Transactional
    @Override
    public void processPost(EncodingCallbackRequestDto encodingCallbackRequestDto) {
        String uploadKey = getKeyFromFilePath(encodingCallbackRequestDto.filePath());
        switch (encodingCallbackRequestDto.status()) {
            case WAITING -> {
                connectPost(uploadKey);
                notifyEncodingStatePort.postEncodeState(uploadKey, ShortsStateEnum.IN_PROGRESS);
            }
            case RUNNING -> {
            }
            case FAILURE -> {
                shortsRepository.updateShortState(uploadKey, ShortsStateEnum.FAIL);
                notifyEncodingStatePort.postEncodeState(uploadKey, ShortsStateEnum.FAIL);
            }
            case COMPLETE -> {
                shortsRepository.updateShortState(uploadKey, ShortsStateEnum.OK);
                notifyEncodingStatePort.postEncodeState(uploadKey, ShortsStateEnum.OK);
            }
            default -> log.error("NCPShortsService|Undefined encoding callback status message: {}",
                    encodingCallbackRequestDto.status());
        }
    }

    private void connectPost(String uploadKey) {
        Shorts shorts = loadShortsPort.findByUploadKey(uploadKey);
        if (shorts == null) {
            final String shortsFileName = generateShortsFileName(uploadKey);
            final String thumbnailFileName = generateThumbnailsFileName(uploadKey);
            shorts = new Shorts(ShortsStateEnum.IN_PROGRESS, uploadKey, shortsFileName, thumbnailFileName);
            saveShortsPort.save(shorts);
        }
    }

    private String getKeyFromFilePath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.indexOf("_"));
    }

    private String generateThumbnailsFileName(String uploadKey) {
        return ncpStorageProperties.getThumbnail().getThumbnailUrlPrefixHead() + uploadKey + ncpStorageProperties.getThumbnail().getThumbnailUrlPrefixTail();
    }

    private String generateShortsFileName(String uploadKey) {
        return ncpStorageProperties.getShorts().getStreamingUrlPrefixHead() + uploadKey + ncpStorageProperties.getShorts().getStreamingUrlPrefixTail();
    }

}
