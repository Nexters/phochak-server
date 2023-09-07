package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.post.adapter.out.persistence.HashtagRepository;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.application.HashtagService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class HashtagUseCaseTest {

    @InjectMocks
    HashtagService hashtagService;

    @Mock HashtagRepository hashtagRepository;

    @Test
    @DisplayName("해시태그 생성 성공")
    void createHashtag() {
        //given
        List<String> stringHashList = List.of("해시태그1", "해시_태그2", "해시태그3");
        PostEntity postEntity = new PostEntity();

        //when
        hashtagService.saveHashtagsByString(stringHashList, postEntity);

        //then
        verify(hashtagRepository, times(1)).saveAll(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("해시태그가 없으면 데이터를 생성하지 않는다.")
    void createHashtagWithEmptyList() {
        //given
        List<String> stringHashList = Collections.emptyList();
        PostEntity postEntity = new PostEntity();

        //when
        hashtagService.saveHashtagsByString(stringHashList, postEntity);

        //then
        verify(hashtagRepository, never()).saveAll(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("해시태그에 공백이 있으면 INVALID_INPUT 예외가 발생한다")
    void createHashtagWithSpace_invalidInput() {
        //given
        List<String> stringHashList = List.of("해시태 그1", " 해시태그2", "해시태그 3");
        PostEntity postEntity = new PostEntity();

        //when, then
        assertThatThrownBy(() -> hashtagService.saveHashtagsByString(stringHashList, postEntity))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("해시태그가 20자가 넘으면 SPACE_IN_HASHTAG 예외가 발생한다")
    void createHashtagWithOverThen20Char_invalidInput() {
        //given
        List<String> stringHashList = List.of("123456789012345678901");
        PostEntity postEntity = new PostEntity();

        //when, then
        assertThatThrownBy(() -> hashtagService.saveHashtagsByString(stringHashList, postEntity))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("해시태그 특수문자 _ 외에는 SPACE_IN_HASHTAG 예외가 발생한다")
    void createHashtagWithExclamationMark_invalidInput() {
        //given
        List<String> stringHashList = List.of("tet@test");
        PostEntity postEntity = new PostEntity();

        //when, then
        assertThatThrownBy(() -> hashtagService.saveHashtagsByString(stringHashList, postEntity))
                .isInstanceOf(PhochakException.class);
    }

}
