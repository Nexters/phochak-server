package com.nexters.phochak.service.impl;

import com.nexters.phochak.config.property.NCPStorageProperties;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.service.ShortsService;
import com.nexters.phochak.specification.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NCPShortsService implements ShortsService {

    private final ShortsRepository shortsRepository;
    private final NCPStorageProperties ncpStorageProperties;

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

    @Override
    @Transactional
    public void connectPost(EncodingCallbackRequestDto encodingCallbackRequestDto) {
        String uploadKey = getKeyFromFilePath(encodingCallbackRequestDto.getFilePath());

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
