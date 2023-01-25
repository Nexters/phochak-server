package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.MediaFileRepository;
import com.nexters.phochak.repository.ShortsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ShortsServiceImpl {

    private final ShortsRepository shortsRepository;
    private final MediaFileRepository mediaFileRepository;

    @Value("app.resource.support.shorts")
    private final List<String> supportedExtension;

    public Shorts createShorts(MultipartFile multipartFile) {
        String extension = getExtension(multipartFile);
        if(!isSupportedExtension(extension)) {
            throw new PhochakException(ResCode.NOT_SUPPORT_VIDEO_EXTENSION);
        }
        String filePath = mediaFileRepository.uploadVideo(multipartFile);
        //ToDo 썸네일 추가
        //ToDo 메타데이터 추출 가능 여부 확인
        return shortsRepository.save(
                Shorts.builder()
                    .shortsUrl(filePath)
                    .thumbnailUrl("추가 예정")
                    .build());
    }

    private boolean isSupportedExtension(String extension) {
        return supportedExtension.contains(extension);
    }

    private String getExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if(!filename.contains(".")) {
            throw new PhochakException(ResCode.NOT_SUPPORT_VIDEO_EXTENSION);
        }
        return filename.substring(filename.indexOf(".") + 1).toLowerCase(Locale.ROOT);
    }
}
