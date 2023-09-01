package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.likes.application.LikeServiceImpl;
import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.likes.domain.LikesRepository;
import com.nexters.phochak.post.adapter.out.persistence.Post;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @InjectMocks
    LikeServiceImpl likeService;

    @Mock UserRepository userRepository;
    @Mock PostRepository postRepository;
    @Mock
    LikesRepository likesRepository;

    @Test
    @DisplayName("포착하기 성공")
    void addPhochak() {
        //given
        UserEntity userEntity = new UserEntity();
        Post post = new Post();
        Likes likes = new Likes(userEntity, post);

        given(userRepository.getReferenceById(anyLong())).willReturn(userEntity);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(likesRepository.save(refEq(likes))).willReturn(likes);

        //then
        likeService.addPhochak(0L, 0L);

        //then
        verify(likesRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("이미 포착된 게시글은 ALREADY_PHOCHAKED 예외가 발생한다")
    void addPhochak_alreadyPhochaked() {
        //given
        UserEntity userEntity = new UserEntity();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(userEntity);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(likesRepository.save(refEq(new Likes(userEntity, post)))).willThrow(DataIntegrityViolationException.class);

        //when, then
        assertThatThrownBy(() -> likeService.addPhochak(0L, 0L))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("포착 취소하기 성공")
    void cancelPhochak() {
        //given
        UserEntity userEntity = new UserEntity();
        Post post = new Post();
        Likes likes = new Likes();

        given(userRepository.getReferenceById(anyLong())).willReturn(userEntity);
        given(postRepository.getReferenceById(anyLong())).willReturn(post);
        given(likesRepository.findByUserAndPost(userEntity, post)).willReturn(Optional.of(likes));

        //then
        likeService.cancelPhochak(0L, 0L);

        //then
        verify(likesRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("포착되지 않았던 게시글을 포착 취소하면 NOT_PHOCHAKED 예외가 발생한다")
    void cancelPhochak_notPhochaked() {
        //given
        UserEntity userEntity = new UserEntity();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(userEntity);
        given(postRepository.getReferenceById(anyLong())).willReturn(post);
        given(likesRepository.findByUserAndPost(userEntity, post)).willReturn(Optional.empty());

        //when, then
        assertThatExceptionOfType(PhochakException.class).isThrownBy(() -> likeService.cancelPhochak(0L, 0L));
        verify(likesRepository, never()).delete(any());
    }
}
