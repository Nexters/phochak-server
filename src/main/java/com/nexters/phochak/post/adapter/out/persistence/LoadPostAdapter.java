package com.nexters.phochak.post.adapter.out.persistence;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.adapter.out.persistence.UserMapper;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadPostAdapter implements LoadPostPort {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    public Post load(final Long postId) {
        final PostEntity postEntity = postRepository.findPostFetchJoin(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));
        return postMapper.toDomain(postEntity);
    }

    @Override
    public List<Post> loadAllPostByUser(final User user) {
        final List<PostEntity> postEntities = postRepository.findAllPostByUserIdFetchJoin(user.getId());
        return postEntities.stream()
                .map(postMapper::toDomain).toList();
    }
}
