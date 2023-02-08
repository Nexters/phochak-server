package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.service.ShortsService;
import com.nexters.phochak.specification.ShortsStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NCPShortsService implements ShortsService {

    private final ShortsRepository shortsRepository;
    private final PostRepository postRepository;
    @Value("${ncp.shorts.streaming-url-prefix.head}")
    private final String STREAMING_PREFIX_HEAD;
    @Value("${ncp.shorts.streaming-url-prefix.tail}")
    private final String STREAMING_PREFIX_TAIL;
    @Value("${ncp.shorts.thumbnail-url-prefix.head}")
    private final String THUMBNAIL_PREFIX_HEAD;
    @Value("${ncp.shorts.thumbnail-url-prefix.tail}")
    private final String THUMBNAIL_PREFIX_TAIL;

    @Override
    public void connectShorts(String uploadKey, Post post) {
        Optional<Shorts> optionalShorts = shortsRepository.findByUploadKey(uploadKey);

        if(optionalShorts.isPresent()) {
            // case: 인코딩이 먼저 끝나있는 경우
            //TODO: s3에 실제 파일이 존재하는지 더블 체크 + 수동 DB 롤백
            Shorts shorts = optionalShorts.get();
            post.setShorts(shorts);
            post.updateShortsState(ShortsStateEnum.OK);
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

        if(optionalShorts.isPresent()) {
            // case: 포스트 생성이 먼저된 경우 -> 상태 변경
            //TODO: s3에 실제 파일이 존재하는지 더블 체크 + 수동 DB 롤백
            Shorts shorts = optionalShorts.get();
            Post post = postRepository.findByShorts(shorts).orElseThrow(() ->
                    new PhochakException(ResCode.INTERNAL_SERVER_ERROR, "중복 쇼츠 데이터 발생"));
            post.updateShortsState(ShortsStateEnum.OK);
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
        return filePath.substring(filePath.lastIndexOf("/")+1, filePath.indexOf("_"));
    }

    private String generateThumbnailsFileName(String uploadKey) {
        return THUMBNAIL_PREFIX_HEAD + uploadKey + THUMBNAIL_PREFIX_TAIL;
    }

    private String generateShortsFileName(String uploadKey) {
        return STREAMING_PREFIX_HEAD + uploadKey + STREAMING_PREFIX_TAIL;
    }


}
