package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;

public interface CountReportPort {
    int count(Post post);
}
