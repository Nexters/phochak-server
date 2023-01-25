package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.repository.MediaFileRepository;
import com.nexters.phochak.repository.ShortsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ShortsServiceImpl {

    private final ShortsRepository shortsRepository;
    private final MediaFileRepository mediaFileRepository;
    public Shorts createShorts(MultipartFile multipartFile) {
        String filePath = mediaFileRepository.uploadVideo(multipartFile);
        //ToDo 썸네일 추가
        //ToDo 메타데이터 추출 가능 여부 확인
        return shortsRepository.save(
                Shorts.builder()
                    .shortsUrl(filePath)
                    .thumbnailUrl("추가 예정")
                    .build());
    }
}
