package com.nexters.phochak.service;

public interface PhochakService {
    void addPhochak(Long userId, Long postId);

    void cancelPhochak(Long userId, Long postId);
}
