package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.likes.application.LikeServiceImpl;
import com.nexters.phochak.likes.domain.Likes;
import com.nexters.phochak.likes.domain.LikesRepository;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.user.domain.User;
import com.nexters.phochak.user.domain.UserRepository;
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
        User user = new User();
        Post post = new Post();
        Likes likes = new Likes(user, post);

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
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
        User user = new User();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(likesRepository.save(refEq(new Likes(user, post)))).willThrow(DataIntegrityViolationException.class);

        //when, then
        assertThatThrownBy(() -> likeService.addPhochak(0L, 0L))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("포착 취소하기 성공")
    void cancelPhochak() {
        //given
        User user = new User();
        Post post = new Post();
        Likes likes = new Likes();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.getReferenceById(anyLong())).willReturn(post);
        given(likesRepository.findByUserAndPost(user, post)).willReturn(Optional.of(likes));

        //then
        likeService.cancelPhochak(0L, 0L);

        //then
        verify(likesRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("포착되지 않았던 게시글을 포착 취소하면 NOT_PHOCHAKED 예외가 발생한다")
    void cancelPhochak_notPhochaked() {
        //given
        User user = new User();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.getReferenceById(anyLong())).willReturn(post);
        given(likesRepository.findByUserAndPost(user, post)).willReturn(Optional.empty());

        //when, then
        assertThatExceptionOfType(PhochakException.class).isThrownBy(() -> likeService.cancelPhochak(0L, 0L));
        verify(likesRepository, never()).delete(any());
    }
}
