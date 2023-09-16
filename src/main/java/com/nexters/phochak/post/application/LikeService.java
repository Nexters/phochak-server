package com.nexters.phochak.post.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.adapter.out.persistence.Likes;
import com.nexters.phochak.post.adapter.out.persistence.LikesRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.application.port.in.LikesFetchDto;
import com.nexters.phochak.post.application.port.in.LikesUseCase;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService implements LikesUseCase {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void addPhochak(Long userId, Long postId) {
        try {
            UserEntity userEntity = userRepository.getReferenceById(userId);
            PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));

            Likes likes = Likes.builder()
                    .user(userEntity)
                    .post(postEntity)
                    .build();
            likesRepository.save(likes);
        } catch (
                DataIntegrityViolationException e) {
            throw new PhochakException(ResCode.ALREADY_PHOCHAKED);
        }
    }

    @Override
    public void cancelPhochak(Long userId, Long postId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = postRepository.getReferenceById(postId);

        Likes likes = likesRepository.findByUserAndPost(userEntity, postEntity)
                .orElseThrow(() -> new PhochakException(ResCode.NOT_PHOCHAKED));

        likesRepository.delete(likes);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId) {
        return likesRepository.checkIsLikedPost(postIds, userId);
    }

}
