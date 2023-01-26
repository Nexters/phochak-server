package com.nexters.phochak.service.impl;

import com.nexters.phochak.domain.Post;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashtagServiceImplTest {

    @InjectMocks
    HashtagServiceImpl hashtagService;

    @Mock HashtagRepository hashtagRepository;

    @Test
    @DisplayName("해시태그 생성 성공")
    void createHashtag() {
        //given
        List<String> stringHashList = List.of("해시태그1", "해시태그2", "해시태그3");
        Post post = new Post();

        //when
        hashtagService.createHashtagsByString(stringHashList, post);

        //then
        verify(hashtagRepository, times(1)).saveAll(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("해시태그가 없으면 데이터를 생성하지 않는다.")
    void createHashtagWithEmptyList() {
        //given
        List<String> stringHashList = Collections.emptyList();
        Post post = new Post();

        //when
        hashtagService.createHashtagsByString(stringHashList, post);

        //then
        verify(hashtagRepository, never()).saveAll(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("해시태그에 공백이 있으면 INVALID_INPUT 예외가 발생한다")
    void createHashtagWithSpace_invalidInput() {
        //given
        List<String> stringHashList = List.of("해시태 그1", " 해시태그2", "해시태그 3");
        Post post = new Post();

        //when, then
        assertThatThrownBy(() -> hashtagService.createHashtagsByString(stringHashList, post))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.INVALID_INPUT.getMessage());
    }

    @Test
    @DisplayName("해시태그가 30개 이상이면 INVALID_INPUT 예외가 발생한다")
    void createHashtagOver30_invalidInput() {
        //given
        List<String> stringHashList = new ArrayList<>();
        for(int i=0;i<31;i++) {
            stringHashList.add("해시태그"+i);
        }
        Post post = new Post();

        //when, then
        assertThatThrownBy(() -> hashtagService.createHashtagsByString(stringHashList, post))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.INVALID_INPUT.getMessage());
    }
}