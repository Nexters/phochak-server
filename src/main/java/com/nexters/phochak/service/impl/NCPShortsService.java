package com.nexters.phochak.service.impl;

import com.nexters.phochak.config.property.NCPStorageProperties;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.service.NotificationService;
import com.nexters.phochak.service.ShortsService;
import com.nexters.phochak.specification.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NCPShortsService implements ShortsService {

    private final ShortsRepository shortsRepository;
    private final NCPStorageProperties ncpStorageProperties;
    private final NotificationService notificationService;

    @Override
    public void connectShorts(String uploadKey, Post post) {
        Optional<Shorts> optionalShorts = shortsRepository.findByUploadKey(uploadKey);

        if (optionalShorts.isPresent()) {
            // case: 인코딩이 먼저 끝나있는 경우
            Shorts shorts = optionalShorts.get();
            shorts.updateShortsState(ShortsStateEnum.OK);
            post.setShorts(shorts);
        } else {
            // case: 인코딩이 끝나지 않은 경우
            String shortsFileName = generateShortsFileName(uploadKey);
            String thumbnailFileName = generateThumbnailsFileName(uploadKey);
            Shorts shorts = Shorts.builder()
                    .uploadKey(uploadKey)
                    .shortsUrl(shortsFileName)
                    .thumbnailUrl(thumbnailFileName)
                    .build();
            shortsRepository.save(shorts);
            post.setShorts(shorts);
        }
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
        String uploadKey = getKeyFromFilePath(encodingCallbackRequestDto.getFilePath());
        switch (encodingCallbackRequestDto.getStatus()) {
            case "WAITING":
                connectPost(uploadKey);
                notificationService.postEncodeState(uploadKey, ShortsStateEnum.IN_PROGRESS);
                break;
            case "RUNNING":
                break;
            case "FAILURE":
                shortsRepository.updateShortState(uploadKey, ShortsStateEnum.FAIL);
                notificationService.postEncodeState(uploadKey, ShortsStateEnum.FAIL);
                break;
            case "COMPLETE":
                shortsRepository.updateShortState(uploadKey, ShortsStateEnum.OK);
                notificationService.postEncodeState(uploadKey, ShortsStateEnum.OK);
                break;
            default:
                log.error("NCPShortsService|Undefined encoding callback status message: {}",
                        encodingCallbackRequestDto.getStatus());
        }
    }

    private void connectPost(String uploadKey) {
        Optional<Shorts> optionalShorts = shortsRepository.findByUploadKey(uploadKey);
        if (optionalShorts.isPresent()) {
            // case: 포스트 생성이 먼저된 경우 -> 상태 변경
            Shorts shorts = optionalShorts.get();
            shorts.updateShortsState(ShortsStateEnum.OK);
        } else {
            // case: 포스트 생성이 되지 않은 경우 -> shorts 만 미리 생성
            String shortsFileName = generateShortsFileName(uploadKey);
            String thumbnailFileName = generateThumbnailsFileName(uploadKey);
            Shorts shorts = Shorts.builder()
                    .uploadKey(uploadKey)
                    .shortsUrl(shortsFileName)
                    .thumbnailUrl(thumbnailFileName)
                    .build();
            shortsRepository.save(shorts);
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
