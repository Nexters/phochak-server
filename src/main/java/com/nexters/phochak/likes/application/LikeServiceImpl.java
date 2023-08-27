package com.nexters.phochak.likes.application;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.likes.LikesFetchDto;
import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.likes.domain.LikesRepository;
import com.nexters.phochak.post.PostFetchCommand;
import com.nexters.phochak.post.PostFetchDto;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.user.domain.UserEntity;
import com.nexters.phochak.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void addPhochak(Long userId, Long postId) {
        try {
            UserEntity userEntity = userRepository.getReferenceById(userId);
            Post post = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));

            Likes likes = Likes.builder()
                    .user(userEntity)
                    .post(post)
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
        Post post = postRepository.getReferenceById(postId);

        Likes likes = likesRepository.findByUserAndPost(userEntity, post)
                .orElseThrow(() -> new PhochakException(ResCode.NOT_PHOCHAKED));

        likesRepository.delete(likes);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId) {
        return likesRepository.checkIsLikedPost(postIds, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostFetchDto> findLikedPostsByCommand(PostFetchCommand command) {
        return likesRepository.findLikedPosts(command);
    }
}
