package com.nexters.phochak.repository;

import org.springframework.web.multipart.MultipartFile;

public interface MediaFileRepository {

    String uploadVideo(MultipartFile video, String extension);
}
