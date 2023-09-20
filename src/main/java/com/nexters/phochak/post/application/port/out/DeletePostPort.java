package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;

public interface DeletePostPort {

    void delete(Post post);

    void deleteAllByUser(User user);
}
