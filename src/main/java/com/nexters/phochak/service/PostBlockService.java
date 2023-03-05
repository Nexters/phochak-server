package com.nexters.phochak.service;

public interface PostBlockService {
    void notifyAndBlockIfRequired(Long postId, Long userId, String reaseon);
}
