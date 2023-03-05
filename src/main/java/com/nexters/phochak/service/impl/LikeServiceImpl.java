package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Likes;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.LikesFetchDto;
import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.PostFetchDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.LikesRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.LikesService;
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
            User user = userRepository.getReferenceById(userId);
            Post post = postRepository.findById(postId).orElseThrow(() -> new PhochakException(ResCode.NOT_FOUND_POST));

            Likes likes = Likes.builder()
                    .user(user)
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
        User user = userRepository.getReferenceById(userId);
        Post post = postRepository.getReferenceById(postId);

        Likes likes = likesRepository.findByUserAndPost(user, post)
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
