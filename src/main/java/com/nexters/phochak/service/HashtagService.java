package com.nexters.phochak.service;

import com.nexters.phochak.domain.Post;

import java.util.List;

public interface HashtagService {
    void createHashtagsByString(List<String> stringHashtagList, Post post);

}
