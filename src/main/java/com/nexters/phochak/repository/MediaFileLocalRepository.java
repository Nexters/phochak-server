package com.nexters.phochak.repository;

import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Repository
public class MediaFileLocalRepository implements MediaFileRepository {

    @Value("${app.resource.local.shorts}")
    private String shortsLocalPath;

    @Override
    public String uploadVideo(MultipartFile shorts) {
        Path filePath = Paths.get(shortsLocalPath + UUID.randomUUID());
        try {
            shorts.transferTo(filePath);
        } catch(IllegalStateException e) {
            throw new PhochakException(ResCode.INVALID_VIDEO_FORMAT, "올바르지 않은 파일");
        } catch(IOException e) {
            throw new PhochakException(ResCode.INTERNAL_SERVER_ERROR, "영상 파일 처리 실패");
        }
        return getVideoUrl(filePath);
    }

    private String getVideoUrl(Path filePath) {
        return shortsLocalPath + filePath.toString();
    }

}