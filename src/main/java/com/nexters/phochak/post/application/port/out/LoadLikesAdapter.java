package com.nexters.phochak.post.application.port.out;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.LikesEntity;
import com.nexters.phochak.post.adapter.out.persistence.LikesMapper;
import com.nexters.phochak.post.adapter.out.persistence.LikesRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostMapper;
import com.nexters.phochak.post.domain.Likes;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadLikesAdapter implements LoadLikesPort {

    private final LikesRepository likesRepository;
    private final LikesMapper likesMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Override
    public Likes load(final User user, final Post post) {
        final LikesEntity likesEntity = likesRepository.findByUserAndPost(
                userMapper.toEntity(user),
                postMapper.toEntity(post))
                .orElseThrow(() -> new PhochakException(ResCode.NOT_PHOCHAKED));
        return likesMapper.toDomain(likesEntity);
    }
}
