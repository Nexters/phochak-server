package com.nexters.phochak.deprecated.service;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.common.exception.ResCode;
import com.nexters.phochak.post.application.PostServiceImpl;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.shorts.PostUploadKeyResponseDto;
import com.nexters.phochak.shorts.presentation.StorageBucketClient;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Disabled
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks PostServiceImpl postService;

    @Mock
    StorageBucketClient storageBucketClient;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("Presigned URL과 UploadKey 생성에 성공한다")
    void generateUploadKey() throws MalformedURLException {
        //given
        given(storageBucketClient.generatePresignedUrl(any())).willReturn(new URL("http://test.com"));

        //when
        PostUploadKeyResponseDto result = postService.generateUploadKey("mov");

        //then
        assertThat(result.getUploadKey()).isNotNull();
    }

    @Test
    @DisplayName("포스트 주인이 아닌 유저가 삭제를 시도하면 NOT_POST_OWNER 예외가 발생한다")
    void deletePostByNotPostOwner() {
        //given
        UserEntity userEntity = new UserEntity();
        UserEntity owner = new UserEntity();
        Post post = Post.builder()
                        .userEntity(owner)
                        .build();
        given(userRepository.getReferenceById(any())).willReturn(userEntity);
        given(postRepository.findPostFetchJoin(any())).willReturn(Optional.of(post));

        //when, then
        assertThatThrownBy(() -> postService.delete(0L, 0L))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.NOT_POST_OWNER.getMessage());
    }
}
