package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Hashtag;

import java.util.List;

public interface SaveHashtagPort {
    void saveAll(List<Hashtag> hashtagList);
}
