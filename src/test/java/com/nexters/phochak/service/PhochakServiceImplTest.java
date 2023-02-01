package com.nexters.phochak.service;

import com.nexters.phochak.domain.Phochak;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.repository.PhochakRepository;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.PhochakServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhochakServiceImplTest {

    @InjectMocks PhochakServiceImpl phochakService;

    @Mock UserRepository userRepository;
    @Mock PostRepository postRepository;
    @Mock PhochakRepository phochakRepository;

    @Test
    @DisplayName("포착하기 성공")
    void addPhochak() {
        //given
        User user = new User();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(phochakRepository.existsByUserAndPost(user, post)).willReturn(false);

        //then
        phochakService.addPhochak(0L, 0L);

        //then
        verify(phochakRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("이미 포착된 게시글은 ALREADY_PHOCHAKED 예외가 발생한다")
    void addPhochak_alreadyPhochaked() {
        //given
        User user = new User();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(phochakRepository.existsByUserAndPost(user, post)).willReturn(true);

        //when, then
        assertThrows(PhochakException.class, () -> {
            phochakService.addPhochak(0L, 0L);
        });
        verify(phochakRepository, never()).save(any());
    }

    @Test
    @DisplayName("포착 취소하기 성공")
    void cancelPhochak() {
        //given
        User user = new User();
        Post post = new Post();
        Phochak phochak = new Phochak();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(phochakRepository.findByUserAndPost(user, post)).willReturn(Optional.of(phochak));

        //then
        phochakService.cancelPhochak(0L, 0L);

        //then
        verify(phochakRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("포착되지 않았던 게시글을 포착 취소하면 NOT_PHOCHAKED 예외가 발생한다")
    void cancelPhochak_notPhochaked() {
        //given
        User user = new User();
        Post post = new Post();

        given(userRepository.getReferenceById(anyLong())).willReturn(user);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(phochakRepository.findByUserAndPost(user, post)).willReturn(Optional.empty());

        //when, then
        assertThrows(PhochakException.class, () -> {
            phochakService.cancelPhochak(0L, 0L);
        });
        verify(phochakRepository, never()).delete(any());
    }
}