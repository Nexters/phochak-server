package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.dto.EncodingCallbackRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ShortsRepository;
import com.nexters.phochak.service.ShortsService;
import com.nexters.phochak.specification.ShortsState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NCPShortsService implements ShortsService {

    private final ShortsRepository shortsRepository;
    private final PostRepository postRepository;

    @Override
    public void connectShorts(String key, Post post) {
        Optional<Shorts> optionalShorts = shortsRepository.findByKey(key);

        if(optionalShorts.isPresent()) {
            // case: 인코딩이 먼저 끝나있는 경우
            //TODO: s3에 실제 파일이 존재하는지 더블 체크 + 수동 DB 롤백
            Shorts shorts = optionalShorts.get();
            post.setShorts(shorts);
            post.updateShortsState(ShortsState.OK);
        } else {
            // case: 인코딩이 끝나지 않은 경우
            String shortsFileName = generateShortsFileName(key);
            String thumbnailFileName = generateThumbnailsFileName(key);
            Shorts shorts = Shorts.builder()
                            .key(key)
                            .shortsUrl(shortsFileName)
                            .thumbnailUrl(thumbnailFileName)
                            .build();
            shortsRepository.save(shorts);
            post.setShorts(shorts);
        }
    }

    @Override
    public void connectPost(EncodingCallbackRequestDto encodingCallbackRequestDto) {
        String key = getKeyFromFilePath(encodingCallbackRequestDto.getFilePath());

        Optional<Shorts> optionalShorts = shortsRepository.findByKey(key);

        if(optionalShorts.isPresent()) {
            // case: 포스트 생성이 먼저된 경우
            //TODO: s3에 실제 파일이 존재하는지 더블 체크 + 수동 DB 롤백
            Shorts shorts = optionalShorts.get();
            Post post = postRepository.findByShorts(shorts).orElseThrow(() ->
                    new PhochakException(ResCode.INTERNAL_SERVER_ERROR, "중복 쇼츠 데이터 발생"));
            post.setShorts(shorts);
            post.updateShortsState(ShortsState.OK);
        } else {
            // case: 포스트 생성이 되지 않은 경우
            String shortsFileName = generateShortsFileName(key);
            String thumbnailFileName = generateThumbnailsFileName(key);
            Shorts shorts = Shorts.builder()
                    .key(key)
                    .shortsUrl(shortsFileName)
                    .thumbnailUrl(thumbnailFileName)
                    .build();
            shortsRepository.save(shorts);
        }
    }

    private String getKeyFromFilePath(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/")+1, filePath.indexOf("_"));
    }

    private String generateThumbnailsFileName(String key) {
        return "https://avvyxbbcswfn15804294.cdn.ntruss.com/hls/DtbTiSqB73qTBrez5H4IJg__/shorts/"+key+"_encoded.mp4/index.m3u8";
    }

    private String generateShortsFileName(String key) {
        return "https://kr.object.ncloudstorage.com/phochak-shorts/thumbnail/shorts/"+key+"_01.jpg";
    }


}
